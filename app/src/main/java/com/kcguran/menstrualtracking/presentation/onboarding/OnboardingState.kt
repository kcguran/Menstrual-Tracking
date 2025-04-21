package com.kcguran.menstrualtracking.presentation.onboarding

data class OnboardingState(
    val name: String = "",
    val surname: String = "",
    val age: String = "",
    val height: String = "",
    val weight: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isComplete: Boolean = false
)