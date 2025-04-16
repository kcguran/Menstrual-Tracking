package com.kcguran.menstrualtracking.domain.repository

import com.kcguran.menstrualtracking.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateUserProfile(userProfile: UserProfile)
}