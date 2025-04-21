// domain/usecase/userprofile/GetCycleStatusUseCase.kt
package com.kcguran.menstrualtracking.domain.usecase.userprofile

import com.kcguran.menstrualtracking.domain.enum.CycleStatus
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.GetMenstrualCyclesUseCase
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.PredictNextCycleUseCase
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetCycleStatusUseCase @Inject constructor(
    private val getMenstrualCyclesUseCase: GetMenstrualCyclesUseCase,
    private val predictNextCycleUseCase: PredictNextCycleUseCase
) {
    /**
     * Kullanıcının güncel regl döngüsü durumunu hesaplar.
     *
     * @return Güncel regl durumu, kalan gün sayısı ve olası semptomlar
     */
    suspend operator fun invoke(): CycleStatusResult {
        val cycles = getMenstrualCyclesUseCase().first().sortedByDescending { it.startDate }
        val predictedCycles = predictNextCycleUseCase(3)
        val today = LocalDate.now()

        var cycleStatus = CycleStatus.NotInPeriod
        var daysUntilNextPeriod: Int? = null
        var daysLeftInCurrentPeriod: Int? = null
        var possibleSymptoms = listOf<String>()

        // Şu anda regl döneminde mi kontrol et
        val currentPeriod = cycles.find { cycle ->
            val endDate = cycle.endDate ?: cycle.startDate.plusDays(cycle.periodLength.toLong() - 1)
            today in cycle.startDate..endDate
        }

        if (currentPeriod != null) {
            // Regl dönemindeyse
            cycleStatus = CycleStatus.InPeriod
            val endDate = currentPeriod.endDate ?: currentPeriod.startDate.plusDays(currentPeriod.periodLength.toLong() - 1)
            daysLeftInCurrentPeriod = ChronoUnit.DAYS.between(today, endDate).toInt() + 1
            possibleSymptoms = getPeriodSymptoms(daysLeftInCurrentPeriod)
        } else {
            // Doğurgan dönemde mi kontrol et
            val fertilityWindow = predictNextCycleUseCase.checkFertilityForDate(today)
            if (fertilityWindow != null) {
                cycleStatus = CycleStatus.Fertile
                possibleSymptoms = getFertileSymptoms()
            }

            // Bir sonraki regl ne zaman
            val nextPeriod = predictedCycles.firstOrNull()
            if (nextPeriod != null) {
                daysUntilNextPeriod = ChronoUnit.DAYS.between(today, nextPeriod.startDate).toInt()

                // Yaklaşan regl için semptomlar
                if (daysUntilNextPeriod <= 7) {
                    possibleSymptoms = getPMSSymptoms()
                }
            }
        }

        return CycleStatusResult(
            cycleStatus = cycleStatus,
            daysUntilNextPeriod = daysUntilNextPeriod,
            daysLeftInCurrentPeriod = daysLeftInCurrentPeriod,
            possibleSymptoms = possibleSymptoms
        )
    }

    private fun getPeriodSymptoms(daysLeft: Int?): List<String> {
        val defaultSymptoms = listOf(
            "Karın krampları",
            "Bel ağrısı",
            "Yorgunluk",
            "Baş ağrısı"
        )

        // Dönemin hangi gününde olduğuna göre yaygın semptomlar
        val periodDaySymptoms = when (daysLeft) {
            1, 2 -> listOf("Ağır kanama", "Şiddetli kramplar")
            3, 4 -> listOf("Orta düzeyde kanama", "Azalan kramplar")
            else -> listOf("Hafif kanama", "Minimum kramplar")
        }

        return periodDaySymptoms + defaultSymptoms
    }

    private fun getFertileSymptoms(): List<String> {
        return listOf(
            "Yumurtlama ağrısı (mittelschmerz)",
            "Servikal mukus değişimleri",
            "Libido artışı",
            "Hafif lekelenme",
            "Göğüslerde hassasiyet"
        )
    }

    private fun getPMSSymptoms(): List<String> {
        return listOf(
            "Duygusal değişimler",
            "Göğüs hassasiyeti",
            "Şişkinlik hissi",
            "Karın krampları",
            "Akne",
            "Baş ağrısı",
            "Yorgunluk ve enerji düşüklüğü"
        )
    }

    data class CycleStatusResult(
        val cycleStatus: CycleStatus,
        val daysUntilNextPeriod: Int?,
        val daysLeftInCurrentPeriod: Int?,
        val possibleSymptoms: List<String>
    )
}