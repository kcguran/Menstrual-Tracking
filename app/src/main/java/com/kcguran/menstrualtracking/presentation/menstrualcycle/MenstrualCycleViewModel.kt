// presentation/menstrualcycle/MenstrualCycleViewModel.kt
package com.kcguran.menstrualtracking.presentation.menstrualcycle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kcguran.menstrualtracking.domain.model.FertilityWindow
import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.AddMenstrualCycleUseCase
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.GetMenstrualCyclesUseCase
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.PredictNextCycleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MenstrualCycleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenstrualCyclesUseCase: GetMenstrualCyclesUseCase,
    private val addMenstrualCycleUseCase: AddMenstrualCycleUseCase,
    private val predictNextCycleUseCase: PredictNextCycleUseCase
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<MenstrualCycleState> = MutableStateFlow(MenstrualCycleState())
    val stateFlow: StateFlow<MenstrualCycleState> = _stateFlow.asStateFlow()

    init {
        loadMenstrualCycles()
        //scheduleNotifications()
    }

    private fun loadMenstrualCycles() {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isLoading = true) }

            try {
                getMenstrualCyclesUseCase().collectLatest { cycles ->
                    val predictedCycles = predictNextCycleUseCase(3)
                    val fertilityWindows = mutableMapOf<Long, FertilityWindow>()

                    // Gerçek ve tahmin edilen döngüler için doğurganlık pencerelerini hesapla
                    (cycles + predictedCycles).forEach { cycle ->
                        fertilityWindows[cycle.id] =
                            predictNextCycleUseCase.calculateFertilityWindow(cycle)
                    }

                    _stateFlow.update { state ->
                        state.copy(
                            menstrualCycles = cycles,
                            predictedCycles = predictedCycles,
                            fertilityWindows = fertilityWindows,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Regl verisi yüklenirken hata oluştu: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    /*
    private fun scheduleNotifications() {
        viewModelScope.launch {
            try {
                scheduleNotificationsUseCase()
            } catch (e: Exception) {
                // Bildirim planlama hatası durumunda loglama yapılabilir
            }
        }
    }

     */

    fun selectDate(date: LocalDate) {
        _stateFlow.update { it.copy(selectedDate = date) }
    }

    fun addMenstrualCycle(startDate: LocalDate, periodLength: Int = 5) {
        viewModelScope.launch {
            try {
                val newCycle = MenstrualCycle(
                    startDate = startDate,
                    endDate = startDate.plusDays(periodLength.toLong() - 1),
                    periodLength = periodLength
                )

                addMenstrualCycleUseCase(newCycle)
                // Bildirimleri tekrar planla
                // scheduleNotifications()
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(error = "Regl dönemi eklenirken hata oluştu: ${e.localizedMessage}")
                }
            }
        }
    }

    /*
    fun deleteMenstrualCycle(id: Long) {
        viewModelScope.launch {
            try {
                deleteMenstrualCycleUseCase(id)
                // Bildirimleri güncelle
                scheduleNotifications()
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(error = "Regl dönemi silinirken hata oluştu: ${e.localizedMessage}")
                }
            }
        }
    }

     */
}