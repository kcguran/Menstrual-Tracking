package com.kcguran.menstrualtracking.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kcguran.menstrualtracking.domain.enum.CycleStatus
import com.kcguran.menstrualtracking.domain.model.UserProfile
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.GetMenstrualCyclesUseCase
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.PredictNextCycleUseCase
import com.kcguran.menstrualtracking.domain.usecase.userprofile.GetUserProfileUseCase
import com.kcguran.menstrualtracking.domain.usecase.userprofile.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getMenstrualCyclesUseCase: GetMenstrualCyclesUseCase,
    private val predictNextCycleUseCase: PredictNextCycleUseCase,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val stateFlow: StateFlow<ProfileState> = _stateFlow.asStateFlow()

    init {
        loadUserProfile()
        updateCycleStatus()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isLoading = true) }

            try {
                getUserProfileUseCase().collectLatest { profile ->
                    _stateFlow.update { state ->
                        state.copy(
                            userProfile = profile,
                            isLoading = false,
                            error = null
                        )
                    }
                    updateCycleStatus()
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Profil yüklenirken hata oluştu: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    private fun updateCycleStatus() {
        viewModelScope.launch {
            try {
                val cycles = getMenstrualCyclesUseCase().first().sortedByDescending { it.startDate }
                val predictedCycles = predictNextCycleUseCase(3)
                val today = LocalDate.now()

                // Güncel durum ve semptomları belirle
                var currentStatus = CycleStatus.NotInPeriod
                var daysUntilNextPeriod: Int? = null
                var daysLeftInCurrentPeriod: Int? = null
                var possibleSymptoms = listOf<String>()

                // Şu anda regl döneminde mi kontrol et
                val currentPeriod = cycles.find { cycle ->
                    val endDate = cycle.endDate ?: cycle.startDate.plusDays(cycle.periodLength.toLong() - 1)
                    !today.isBefore(cycle.startDate) && !today.isAfter(endDate)
                }

                if (currentPeriod != null) {
                    // Regl dönemindeyse
                    currentStatus = CycleStatus.InPeriod
                    val endDate = currentPeriod.endDate ?: currentPeriod.startDate.plusDays(currentPeriod.periodLength.toLong() - 1)
                    daysLeftInCurrentPeriod = ChronoUnit.DAYS.between(today, endDate).toInt() + 1
                    possibleSymptoms = getPeriodSymptoms(daysLeftInCurrentPeriod, _stateFlow.value.userProfile.commonSymptoms)
                } else {
                    // Doğurgan dönemde mi kontrol et
                    val fertilityWindow = predictNextCycleUseCase.checkFertilityForDate(today)
                    if (fertilityWindow != null) {
                        currentStatus = CycleStatus.Fertile
                        possibleSymptoms = getFertileSymptoms()
                    }

                    // Bir sonraki regl ne zaman
                    val nextPeriod = predictedCycles.firstOrNull()
                    if (nextPeriod != null) {
                        daysUntilNextPeriod = ChronoUnit.DAYS.between(today, nextPeriod.startDate).toInt()

                        // Yaklaşan regl için semptomlar
                        if (daysUntilNextPeriod <= 7) {
                            possibleSymptoms = getPMSSymptoms()
                        }
                    }
                }

                _stateFlow.update { state ->
                    state.copy(
                        currentCycleStatus = currentStatus,
                        daysUntilNextPeriod = daysUntilNextPeriod,
                        daysLeftInCurrentPeriod = daysLeftInCurrentPeriod,
                        possibleSymptoms = possibleSymptoms
                    )
                }

            } catch (e: Exception) {
                // Hata durumunda loglama
            }
        }
    }

    private fun getPeriodSymptoms(daysLeft: Int?, commonSymptoms: List<String>): List<String> {
        val defaultSymptoms = listOf(
            "Karın krampları",
            "Bel ağrısı",
            "Yorgunluk",
            "Baş ağrısı"
        )

        // Dönemin hangi gününde olduğuna göre yaygın semptomlar
        val periodDaySymptoms = when (daysLeft) {
            1, 2 -> listOf("Ağır kanama", "Şiddetli kramplar")
            3, 4 -> listOf("Orta düzeyde kanama", "Azalan kramplar")
            else -> listOf("Hafif kanama", "Minimum kramplar")
        }

        return (periodDaySymptoms + commonSymptoms + defaultSymptoms).distinct()
    }

    private fun getFertileSymptoms(): List<String> {
        return listOf(
            "Yumurtlama ağrısı (mittelschmerz)",
            "Servikal mukus değişimleri",
            "Libido artışı",
            "Hafif lekelenme",
            "Göğüslerde hassasiyet"
        )
    }

    private fun getPMSSymptoms(): List<String> {
        return listOf(
            "Duygusal değişimler",
            "Göğüs hassasiyeti",
            "Şişkinlik hissi",
            "Karın krampları",
            "Akne",
            "Baş ağrısı",
            "Yorgunluk ve enerji düşüklüğü"
        )
    }

    fun updateProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isLoading = true, isSaved = false) }

            try {
                updateUserProfileUseCase(userProfile)

                // Bildirim ayarları değiştiyse bildirimleri yeniden planla
                //scheduleNotificationsUseCase()

                _stateFlow.update { state ->
                    state.copy(
                        userProfile = userProfile,
                        isLoading = false,
                        error = null,
                        isSaved = true
                    )
                }

                // Profil güncellendikten sonra döngü durumunu güncelle
                updateCycleStatus()

            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Profil güncellenirken hata oluştu: ${e.localizedMessage}",
                        isSaved = false
                    )
                }
            }
        }
    }

    fun updateName(name: String) {
        _stateFlow.update {
            it.copy(userProfile = it.userProfile.copy(name = name), isSaved = false)
        }
    }

    fun updateAge(age: Int) {
        _stateFlow.update {
            it.copy(userProfile = it.userProfile.copy(age = age), isSaved = false)
        }
    }

    fun updateAverageCycleLength(length: Int) {
        _stateFlow.update {
            it.copy(userProfile = it.userProfile.copy(averageCycleLength = length), isSaved = false)
        }
    }

    fun updateAveragePeriodLength(length: Int) {
        _stateFlow.update {
            it.copy(userProfile = it.userProfile.copy(averagePeriodLength = length), isSaved = false)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            val updatedProfile = _stateFlow.value.userProfile.copy(notificationsEnabled = enabled)
            _stateFlow.update {
                it.copy(userProfile = updatedProfile, isSaved = false)
            }

            // Bildirimleri hemen güncelle
            if (enabled) {
                //scheduleNotificationsUseCase()
            }
        }
    }

    fun updateCommonSymptoms(symptoms: List<String>) {
        _stateFlow.update {
            it.copy(userProfile = it.userProfile.copy(commonSymptoms = symptoms), isSaved = false)
        }
    }

    fun updateLastPeriodDate(date: LocalDate?) {
        _stateFlow.update {
            it.copy(userProfile = it.userProfile.copy(lastPeriodStartDate = date), isSaved = false)
        }
        updateCycleStatus()
    }
}