package com.kcguran.menstrualtracking.presentation.calendar

import java.time.LocalDate
import java.time.YearMonth

data class CalendarActions(
    val onDateSelected: (LocalDate) -> Unit = {},
    val onMonthChanged: (YearMonth) -> Unit = {},
    val onAddCycleClicked: (LocalDate) -> Unit = {},
    val onNavigateBack: () -> Unit = {},
    val onShowDeleteConfirmDialog: () -> Unit = {},
    val onHideDeleteConfirmDialog: () -> Unit = {},
    val onDeleteAllCycles: () -> Unit = {}
)