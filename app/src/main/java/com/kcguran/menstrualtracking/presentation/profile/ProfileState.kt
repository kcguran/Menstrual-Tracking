package com.kcguran.menstrualtracking.presentation.profile

import com.kcguran.menstrualtracking.domain.enum.CycleStatus
import com.kcguran.menstrualtracking.domain.model.UserProfile

data class ProfileState(
    val userProfile: UserProfile = UserProfile(),
    val currentCycleStatus: CycleStatus = CycleStatus.NotInPeriod,
    val daysUntilNextPeriod: Int? = null,
    val daysLeftInCurrentPeriod: Int? = null,
    val possibleSymptoms: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)
