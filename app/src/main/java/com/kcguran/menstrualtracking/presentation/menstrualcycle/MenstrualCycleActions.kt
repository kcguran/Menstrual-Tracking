package com.kcguran.menstrualtracking.presentation.menstrualcycle

import java.time.LocalDate

data class MenstrualCycleActions(
    val onDateSelected: (LocalDate) -> Unit = {},
    val onAddCycleClicked: (LocalDate, Int) -> Unit = { _, _ -> },
    val onNavigateToCalendar: () -> Unit = {},
    val onNavigateToProfile: () -> Unit = {}
)