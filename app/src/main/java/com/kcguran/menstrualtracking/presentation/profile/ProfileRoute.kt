package com.kcguran.menstrualtracking.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun ProfileRoute(
    navController: NavController,
    coordinator: ProfileCoordinator = rememberProfileCoordinator(
        navController = navController
    )
) {
    // State observing and declarations
    val uiState by coordinator.screenStateFlow.collectAsState(ProfileState())

    // UI Actions
    val actions = rememberProfileActions(coordinator)

    // UI Rendering
    ProfileScreen(uiState, actions)
}

@Composable
fun rememberProfileActions(coordinator: ProfileCoordinator): ProfileActions {
    return remember(coordinator) {
        ProfileActions(
            onNavigateBack = coordinator::navigateBack,
            //onNavigateToSettings = coordinator::navigateToSettings
        )
    }
}