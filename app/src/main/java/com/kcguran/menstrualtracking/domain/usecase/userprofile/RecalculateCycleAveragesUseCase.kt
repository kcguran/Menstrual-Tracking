package com.kcguran.menstrualtracking.domain.usecase.userprofile

import com.kcguran.menstrualtracking.domain.repository.MenstrualCycleRepository
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.first
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class RecalculateCycleAveragesUseCase @Inject constructor(
    private val menstrualCycleRepository: MenstrualCycleRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke() {
        val userProfile = userProfileRepository.getUserProfile().first()
        val cycles = menstrualCycleRepository.getAllMenstrualCycles().first().sortedBy { it.startDate }

        // Eğer yeterli veri yoksa varsayılan değerleri kullan
        if (cycles.size < 2) {
            return  // Değişiklik yapma
        }

        // Döngü uzunluklarını hesapla
        val cycleLengths = mutableListOf<Int>()
        for (i in 0 until cycles.size - 1) {
            val daysBetween = ChronoUnit.DAYS.between(
                cycles[i].startDate,
                cycles[i + 1].startDate
            ).toInt()

            // 15-45 gün aralığındaki değerleri kabul et
            if (daysBetween in 15..45) {
                cycleLengths.add(daysBetween)
            }
        }

        // Regl sürelerini hesapla
        val periodLengths = cycles.mapNotNull { cycle ->
            cycle.endDate?.let {
                ChronoUnit.DAYS.between(cycle.startDate, it).toInt() + 1
            } ?: cycle.periodLength
        }

        // Ortalama değerleri hesapla, yeterli veri yoksa varsayılanları kullan
        val avgCycleLength = if (cycleLengths.isNotEmpty()) cycleLengths.sum() / cycleLengths.size else userProfile.averageCycleLength
        val avgPeriodLength = if (periodLengths.isNotEmpty()) periodLengths.sum() / periodLengths.size else userProfile.averagePeriodLength

        // Profili güncelle
        userProfileRepository.updateUserProfile(
            userProfile.copy(
                averageCycleLength = avgCycleLength,
                averagePeriodLength = avgPeriodLength
            )
        )
    }
}