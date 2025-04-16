package com.kcguran.menstrualtracking.domain.model

import java.time.LocalDate

data class MenstrualCycle(
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val cycleLength: Int = 28,
    val periodLength: Int = 5,
    val symptoms: List<String> = emptyList(),
    val notes: String = ""
)