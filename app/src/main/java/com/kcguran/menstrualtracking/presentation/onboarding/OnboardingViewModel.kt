// presentation/onboarding/OnboardingViewModel.kt
package com.kcguran.menstrualtracking.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kcguran.menstrualtracking.domain.model.UserProfile
import com.kcguran.menstrualtracking.domain.usecase.userprofile.GetUserProfileUseCase
import com.kcguran.menstrualtracking.domain.usecase.userprofile.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(OnboardingState())
    val stateFlow: StateFlow<OnboardingState> = _stateFlow.asStateFlow()

    init {
        checkIfOnboardingRequired()
    }

    private fun checkIfOnboardingRequired() {
        viewModelScope.launch {
            val userProfile = getUserProfileUseCase().first()
            if (!userProfile.isFirstLogin) {
                _stateFlow.update { it.copy(isComplete = true) }
            }
        }
    }

    fun updateName(name: String) {
        _stateFlow.update { it.copy(name = name) }
    }

    fun updateSurname(surname: String) {
        _stateFlow.update { it.copy(surname = surname) }
    }

    fun updateAge(age: String) {
        _stateFlow.update { it.copy(age = age) }
    }

    fun updateHeight(height: String) {
        _stateFlow.update { it.copy(height = height) }
    }

    fun updateWeight(weight: String) {
        _stateFlow.update { it.copy(weight = weight) }
    }

    fun saveUserInfo() {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isLoading = true) }

            try {
                val currentState = _stateFlow.value

                // Değerleri doğrula ve dönüştür
                val age = currentState.age.toIntOrNull() ?: 0
                val height = currentState.height.toIntOrNull() ?: 0
                val weight = currentState.weight.toIntOrNull() ?: 0

                if (currentState.name.isBlank()) {
                    _stateFlow.update {
                        it.copy(
                            isLoading = false,
                            error = "Lütfen adınızı girin"
                        )
                    }
                    return@launch
                }

                if (age <= 0) {
                    _stateFlow.update {
                        it.copy(
                            isLoading = false,
                            error = "Lütfen geçerli bir yaş girin"
                        )
                    }
                    return@launch
                }

                // Mevcut profili al ve güncelle
                val userProfile = getUserProfileUseCase().first()
                val updatedProfile = userProfile.copy(
                    name = currentState.name,
                    surname = currentState.surname,
                    age = age,
                    height = height,
                    weight = weight,
                    isFirstLogin = false  // İlk giriş tamamlandı
                )

                // Profili kaydet
                updateUserProfileUseCase(updatedProfile)

                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        isComplete = true
                    )
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Bilgiler kaydedilirken hata oluştu: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}