package com.kcguran.menstrualtracking.domain.usecase.menstrualcycle

import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import com.kcguran.menstrualtracking.domain.repository.MenstrualCycleRepository
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

class AddMenstrualCycleUseCase @Inject constructor(
    private val menstrualCycleRepository: MenstrualCycleRepository,
    private val userProfileRepository: UserProfileRepository,
) {
    suspend operator fun invoke(menstrualCycle: MenstrualCycle): Long {
        val userProfile = userProfileRepository.getUserProfile().first()

        // Eğer bitiş tarihi belirtilmemiş ise, profildeki ortalama dönem süresini kullan
        val completeModel = if (menstrualCycle.endDate == null) {
            menstrualCycle.copy(
                endDate = menstrualCycle.startDate.plusDays(userProfile.averagePeriodLength.toLong() - 1),
                cycleLength = userProfile.averageCycleLength,
                periodLength = userProfile.averagePeriodLength
            )
        } else {
            menstrualCycle
        }

        // Cycle ekle ve ID'yi al
        val id = menstrualCycleRepository.addMenstrualCycle(completeModel)

        /*
        // Eğer bildirimler aktifse, bir sonraki dönem için bildirim planla
        if (userProfile.notificationsEnabled) {
            val nextPeriodStartDate = completeModel.startDate.plusDays(userProfile.averageCycleLength.toLong())
            scheduleNotifications(nextPeriodStartDate)
        }

         */

        return id
    }

    private fun scheduleNotifications(nextPeriodStartDate: LocalDate) {
        // Burada bildirim planlama işlemleri yapılabilir
        // Örneğin WorkManager veya AlarmManager kullanarak bildirimler ayarlanabilir
    }
}