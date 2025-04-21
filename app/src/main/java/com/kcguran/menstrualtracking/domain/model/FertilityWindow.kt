package com.kcguran.menstrualtracking.domain.model

import java.time.LocalDate

data class FertilityWindow(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val ovulationDate: LocalDate
)