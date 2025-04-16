package com.kcguran.menstrualtracking.presentation.menstrualcycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

class MenstrualCycleCoordinator(
    val viewModel: MenstrualCycleViewModel,
    private val navController: NavController
) {
    val screenStateFlow = viewModel.stateFlow

    fun onDateSelected(date: java.time.LocalDate) {
        viewModel.selectDate(date)
    }

    fun onAddCycleClicked(date: java.time.LocalDate, periodLength: Int) {
        viewModel.addMenstrualCycle(date, periodLength)
    }

    fun navigateToCalendar() {
        navController.navigate("calendar")
    }

    fun navigateToProfile() {
        navController.navigate("profile")
    }
}

@Composable
fun rememberMenstrualCycleCoordinator(
    viewModel: MenstrualCycleViewModel = hiltViewModel(),
    navController: NavController
): MenstrualCycleCoordinator {
    return remember(viewModel, navController) {
        MenstrualCycleCoordinator(
            viewModel = viewModel,
            navController = navController
        )
    }
}