package com.kcguran.menstrualtracking.domain.usecase.menstrualcycle

import com.kcguran.menstrualtracking.domain.repository.MenstrualCycleRepository
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class DeleteAllMenstrualCyclesUseCase @Inject constructor(
    private val menstrualCycleRepository: MenstrualCycleRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke() {
        menstrualCycleRepository.deleteAllMenstrualCycles()

        val currentProfile = userProfileRepository.getUserProfile().first()
        userProfileRepository.updateUserProfile(currentProfile.copy(lastPeriodStartDate = null))
    }
}