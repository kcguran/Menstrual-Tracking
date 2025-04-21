package com.kcguran.menstrualtracking.presentation.menstrualcycle

import com.kcguran.menstrualtracking.domain.model.FertilityWindow
import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import java.time.LocalDate

data class MenstrualCycleState(
    val menstrualCycles: List<MenstrualCycle> = emptyList(),
    val predictedCycles: List<MenstrualCycle> = emptyList(),
    val fertilityWindows: Map<Long, FertilityWindow> = emptyMap(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = false,
    val error: String? = null
)