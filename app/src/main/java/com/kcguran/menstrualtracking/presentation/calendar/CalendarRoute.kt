package com.kcguran.menstrualtracking.presentation.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun CalendarRoute(
    navController: NavController,
    coordinator: CalendarCoordinator = rememberCalendarCoordinator(
        navController = navController
    )
) {
    // State observing and declarations
    val uiState by coordinator.screenStateFlow.collectAsState(CalendarState())

    // UI Actions
    val actions = rememberCalendarActions(coordinator)

    // UI Rendering
    CalendarScreen(uiState, actions)
}

@Composable
fun rememberCalendarActions(coordinator: CalendarCoordinator): CalendarActions {
    return remember(coordinator) {
        CalendarActions(
            onDateSelected = coordinator::onDateSelected,
            onMonthChanged = coordinator::onMonthChanged,
            onAddCycleClicked = coordinator::navigateToAddCycle,
            onNavigateBack = coordinator::navigateBack,
            onShowDeleteConfirmDialog = coordinator::showDeleteConfirmDialog,
            onHideDeleteConfirmDialog = coordinator::hideDeleteConfirmDialog,
            onDeleteAllCycles = coordinator::deleteAllCycles
        )
    }
}