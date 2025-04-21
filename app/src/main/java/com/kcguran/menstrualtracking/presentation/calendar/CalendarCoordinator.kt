package com.kcguran.menstrualtracking.presentation.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kcguran.menstrualtracking.presentation.navigation.Route
import java.time.LocalDate
import java.time.YearMonth

class CalendarCoordinator(
    val viewModel: CalendarViewModel,
    private val navController: NavController
) {
    val screenStateFlow = viewModel.stateFlow

    fun onDateSelected(date: LocalDate) {
        viewModel.selectDate(date)
    }

    fun onMonthChanged(month: YearMonth) {
        viewModel.changeMonth(month)
    }

    fun checkCurrentFertilityStatus() {
        viewModel.checkCurrentFertilityStatus()
    }

    fun navigateToAddCycle(date: LocalDate) {
        // Regl takip ekranına tarih parametresi ile yönlendir
        navController.navigate("${Route.MenstrualCycle.route}?selected_date=${date}")
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    // Yeni eklenen metodlar

    fun showDeleteConfirmDialog() {
        viewModel.showDeleteConfirmDialog()
    }

    fun hideDeleteConfirmDialog() {
        viewModel.hideDeleteConfirmDialog()
    }

    fun deleteAllCycles() {
        viewModel.deleteAllCycles()
    }
}

@Composable
fun rememberCalendarCoordinator(
    viewModel: CalendarViewModel = hiltViewModel(),
    navController: NavController
): CalendarCoordinator {
    return remember(viewModel, navController) {
        CalendarCoordinator(
            viewModel = viewModel,
            navController = navController
        )
    }
}