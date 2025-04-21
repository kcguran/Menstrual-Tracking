package com.kcguran.menstrualtracking.domain.model

import java.time.LocalDate

data class UserProfile(
    val id: Long = 1,
    val name: String = "",
    val surname: String = "",
    val age: Int = 0,
    val height: Int = 0,  // cm cinsinden
    val weight: Int = 0,  // kg cinsinden
    val averageCycleLength: Int = 28,  // Ortalama döngü süresi (gün)
    val averagePeriodLength: Int = 5,  // Ortalama regl süresi (gün)
    val notificationsEnabled: Boolean = true,
    val commonSymptoms: List<String> = emptyList(),
    val lastPeriodStartDate: LocalDate? = null,
    val isFirstLogin: Boolean = true  // İlk giriş durumu
)