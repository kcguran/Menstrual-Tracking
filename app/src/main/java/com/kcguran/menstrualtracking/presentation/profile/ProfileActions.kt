package com.kcguran.menstrualtracking.presentation.profile

data class ProfileActions(
    val onNavigateBack: () -> Unit = {},
    val onNavigateToSettings: () -> Unit = {}
)