package com.kcguran.menstrualtracking.presentation.onboarding

data class OnboardingActions(
    val onNameChanged: (String) -> Unit,
    val onSurnameChanged: (String) -> Unit,
    val onAgeChanged: (String) -> Unit,
    val onHeightChanged: (String) -> Unit,
    val onWeightChanged: (String) -> Unit,
    val onSaveClicked: () -> Unit
)