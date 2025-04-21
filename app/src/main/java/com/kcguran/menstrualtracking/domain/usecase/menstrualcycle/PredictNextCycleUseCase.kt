package com.kcguran.menstrualtracking.domain.usecase.menstrualcycle

import com.kcguran.menstrualtracking.domain.model.FertilityWindow
import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import com.kcguran.menstrualtracking.domain.repository.MenstrualCycleRepository
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

class PredictNextCycleUseCase @Inject constructor(
    private val menstrualCycleRepository: MenstrualCycleRepository,
    private val userProfileRepository: UserProfileRepository
) {
    /**
     * Gelecekteki muhtemel regl dönemlerini tahmin eder.
     * @param count Tahmin edilecek dönem sayısı
     * @return Tahmin edilen regl dönemleri listesi
     */
    suspend operator fun invoke(count: Int = 3): List<MenstrualCycle> {
        val cycles = menstrualCycleRepository.getAllMenstrualCycles().first()
            .sortedByDescending { it.startDate }

        val userProfile = userProfileRepository.getUserProfile().first()

        if (cycles.isEmpty()) {
            return emptyList()
        }

        val lastCycle = cycles.first()

        // Eğer en az 3 döngü varsa, son 3 döngünün ortalamasını al
        val averageCycleLength = if (cycles.size >= 3) {
            val lastThreeCycles = cycles.take(3)
            val totalDays = lastThreeCycles.mapIndexed { index, cycle ->
                if (index < lastThreeCycles.size - 1) {
                    val nextCycle = lastThreeCycles[index + 1]
                    (cycle.startDate.toEpochDay() - nextCycle.startDate.toEpochDay()).toInt()
                } else {
                    userProfile.averageCycleLength
                }
            }.sum()

            totalDays / lastThreeCycles.size
        } else {
            userProfile.averageCycleLength
        }

        // Ortalama dönem uzunluğunu kullanıcı profilinden al veya varsayılan değeri kullan
        val periodLength = userProfile.averagePeriodLength

        return List(count) { index ->
            val startDate = lastCycle.startDate.plusDays(averageCycleLength.toLong() * (index + 1))
            val endDate = startDate.plusDays(periodLength.toLong() - 1)

            MenstrualCycle(
                startDate = startDate,
                endDate = endDate,
                cycleLength = averageCycleLength,
                periodLength = periodLength
            )
        }
    }

    /**
     * Bir regl döngüsü için doğurganlık penceresini hesaplar.
     * Standart bir 28 günlük döngüde, yumurtlama genellikle döngünün 14. gününde olur.
     * Doğurganlık penceresi ise yumurtlamadan 5 gün önce ve 1 gün sonrasını kapsar.
     *
     * @param menstrualCycle Regl döngüsü
     * @return Hesaplanan doğurganlık penceresi
     */
    suspend fun calculateFertilityWindow(menstrualCycle: MenstrualCycle): FertilityWindow {
        val userProfile = userProfileRepository.getUserProfile().first()
        val cycleLength = menstrualCycle.cycleLength

        // Yumurtlama günü hesapla (tipik olarak bir sonraki döngüden 14 gün önce)
        val daysBeforeNextPeriod = 14
        val ovulationDate = menstrualCycle.startDate.plusDays((cycleLength - daysBeforeNextPeriod).toLong())

        // Doğurgan dönem yumurtlamadan 5 gün önce başlar ve 1 gün sonra biter
        val fertileStart = ovulationDate.minusDays(5)
        val fertileEnd = ovulationDate.plusDays(1)

        return FertilityWindow(
            startDate = fertileStart,
            endDate = fertileEnd,
            ovulationDate = ovulationDate
        )
    }

    /**
     * Bir tarih için doğurganlık durumunu kontrol eder.
     *
     * @param date Kontrol edilecek tarih
     * @return İlgili doğurganlık penceresi veya null
     */
    suspend fun checkFertilityForDate(date: LocalDate): FertilityWindow? {
        val cycles = menstrualCycleRepository.getAllMenstrualCycles().first()
            .sortedByDescending { it.startDate }

        if (cycles.isEmpty()) return null

        // Tüm dönemlerin doğurganlık pencerelerini hesapla ve
        // verilen tarihin herhangi birinin içinde olup olmadığını kontrol et
        for (cycle in cycles) {
            val fertilityWindow = calculateFertilityWindow(cycle)
            if (date.isEqual(fertilityWindow.startDate) ||
                date.isEqual(fertilityWindow.endDate) ||
                (date.isAfter(fertilityWindow.startDate) && date.isBefore(fertilityWindow.endDate))) {
                return fertilityWindow
            }
        }

        return null
    }
}