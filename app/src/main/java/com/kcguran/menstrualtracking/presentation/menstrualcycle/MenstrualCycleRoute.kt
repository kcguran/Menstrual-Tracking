package com.kcguran.menstrualtracking.presentation.menstrualcycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun MenstrualCycleRoute(
    navController: NavController,
    coordinator: MenstrualCycleCoordinator = rememberMenstrualCycleCoordinator(
        navController = navController
    )
) {
    // State observing and declarations
    val uiState by coordinator.screenStateFlow.collectAsState(MenstrualCycleState())

    // UI Actions
    val actions = rememberMenstrualCycleActions(coordinator)

    // UI Rendering
    MenstrualCycleScreen(uiState, actions)
}

@Composable
fun rememberMenstrualCycleActions(coordinator: MenstrualCycleCoordinator): MenstrualCycleActions {
    return remember(coordinator) {
        MenstrualCycleActions(
            onDateSelected = coordinator::onDateSelected,
            onAddCycleClicked = coordinator::onAddCycleClicked,
            onNavigateToCalendar = coordinator::navigateToCalendar,
            onNavigateToProfile = coordinator::navigateToProfile
        )
    }
}