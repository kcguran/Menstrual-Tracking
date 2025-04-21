package com.kcguran.menstrualtracking.domain.usecase.userprofile

import com.kcguran.menstrualtracking.domain.model.UserProfile
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    /**
     * Kullanıcı profilini getirir. Eğer profil henüz oluşturulmamışsa
     * varsayılan değerlerle bir profil döndürür.
     */
    operator fun invoke(): Flow<UserProfile> {
        return userProfileRepository.getUserProfile().map { profile ->
            // Eğer profile null ise, varsayılan değerlerle bir profil döndür
            profile ?: UserProfile(
                name = "",
                age = 0,
                averageCycleLength = 28,
                averagePeriodLength = 5,
                notificationsEnabled = true,
                commonSymptoms = emptyList(),
                lastPeriodStartDate = null
            )
        }
    }
}