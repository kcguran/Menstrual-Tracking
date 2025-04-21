package com.kcguran.menstrualtracking.domain.usecase.userprofile

import com.kcguran.menstrualtracking.domain.model.UserProfile
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
) {
    /**
     * Kullanıcı profilini günceller ve güncelleme sonrası
     * bildirimleri yeniden planlar.
     *
     * @param userProfile Güncellenmiş kullanıcı profili
     */
    suspend operator fun invoke(userProfile: UserProfile) {
        userProfileRepository.updateUserProfile(userProfile)
    }
}