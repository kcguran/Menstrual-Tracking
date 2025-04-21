// presentation/onboarding/OnboardingRoute.kt
package com.kcguran.menstrualtracking.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun OnboardingRoute(
    navController: NavController,
    coordinator: OnboardingCoordinator = rememberOnboardingCoordinator(
        navController = navController
    )
) {
    // State observing and declarations
    val uiState by coordinator.screenStateFlow.collectAsState()

    // Navigasyon durumunu kontrol et
    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            coordinator.navigateToMain()
        }
    }

    // UI Actions
    val actions = rememberOnboardingActions(coordinator)

    // UI Rendering
    OnboardingScreen(uiState, actions)
}

@Composable
fun rememberOnboardingActions(coordinator: OnboardingCoordinator): OnboardingActions {
    return remember(coordinator) {
        OnboardingActions(
            onNameChanged = coordinator::onNameChanged,
            onSurnameChanged = coordinator::onSurnameChanged,
            onAgeChanged = coordinator::onAgeChanged,
            onHeightChanged = coordinator::onHeightChanged,
            onWeightChanged = coordinator::onWeightChanged,
            onSaveClicked = coordinator::onSaveClicked
        )
    }
}