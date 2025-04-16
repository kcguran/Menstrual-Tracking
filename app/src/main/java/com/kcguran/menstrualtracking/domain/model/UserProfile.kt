package com.kcguran.menstrualtracking.domain.model

data class UserProfile(
    val id: Long = 1,
    val name: String = "",
    val age: Int = 0,
    val averageCycleLength: Int = 28,
    val averagePeriodLength: Int = 5,
    val notificationsEnabled: Boolean = true
)