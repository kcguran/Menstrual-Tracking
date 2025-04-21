// domain/usecase/userprofile/CalculateAverageCycleLengthUseCase.kt
package com.kcguran.menstrualtracking.domain.usecase.userprofile

import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.GetMenstrualCyclesUseCase
import kotlinx.coroutines.flow.first
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CalculateAverageCycleLengthUseCase @Inject constructor(
    private val getMenstrualCyclesUseCase: GetMenstrualCyclesUseCase
) {
    /**
     * Kullanıcının geçmiş regl döngülerini kullanarak ortalama döngü uzunluğunu hesaplar.
     *
     * @return Ortalama döngü uzunluğu (gün olarak)
     */
    suspend operator fun invoke(): Int {
        val cycles = getMenstrualCyclesUseCase().first()
            .sortedBy { it.startDate }

        // 2'den az döngü varsa hesaplama yapılamaz
        if (cycles.size < 2) {
            return 28 // Varsayılan değer
        }

        // Ardışık döngüler arasındaki gün sayısını hesapla
        val cycleLengths = mutableListOf<Int>()
        for (i in 0 until cycles.size - 1) {
            val currentCycle = cycles[i]
            val nextCycle = cycles[i + 1]

            val daysBetween = ChronoUnit.DAYS.between(
                currentCycle.startDate,
                nextCycle.startDate
            ).toInt()

            // Geçerli bir döngü uzunluğu (15-45 gün arasında)
            if (daysBetween in 15..45) {
                cycleLengths.add(daysBetween)
            }
        }

        // Eğer hesaplanabilir döngü yoksa varsayılan değer döndür
        if (cycleLengths.isEmpty()) {
            return 28
        }

        // Ortalama döngü uzunluğunu hesapla ve yuvarlak bir sayı olarak döndür
        return cycleLengths.sum() / cycleLengths.size
    }
}