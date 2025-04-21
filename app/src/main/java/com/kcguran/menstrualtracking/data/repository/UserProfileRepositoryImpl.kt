package com.kcguran.menstrualtracking.data.repository

import com.kcguran.menstrualtracking.data.local.dao.UserProfileDao
import com.kcguran.menstrualtracking.data.local.entity.UserProfileEntity
import com.kcguran.menstrualtracking.domain.model.UserProfile
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao
) : UserProfileRepository {

    override fun getUserProfile(): Flow<UserProfile> {
        return userProfileDao.getUserProfile().map { entity ->
            entity?.toDomainModel() ?: UserProfile()
        }
    }

    override suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(UserProfileEntity.fromDomainModel(userProfile))
    }
}