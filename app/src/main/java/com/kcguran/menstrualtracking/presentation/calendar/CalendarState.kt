package com.kcguran.menstrualtracking.presentation.calendar

import com.kcguran.menstrualtracking.domain.model.FertilityWindow
import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import java.time.LocalDate
import java.time.YearMonth

data class CalendarState(
    val menstrualCycles: List<MenstrualCycle> = emptyList(),
    val predictedCycles: List<MenstrualCycle> = emptyList(),
    val fertilityWindows: Map<Long, FertilityWindow> = emptyMap(),
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val currentFertilityWindow: FertilityWindow? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)