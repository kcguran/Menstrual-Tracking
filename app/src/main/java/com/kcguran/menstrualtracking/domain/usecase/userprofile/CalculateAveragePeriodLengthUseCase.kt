// domain/usecase/userprofile/CalculateAveragePeriodLengthUseCase.kt
package com.kcguran.menstrualtracking.domain.usecase.userprofile

import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.GetMenstrualCyclesUseCase
import kotlinx.coroutines.flow.first
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CalculateAveragePeriodLengthUseCase @Inject constructor(
    private val getMenstrualCyclesUseCase: GetMenstrualCyclesUseCase
) {
    /**
     * Kullanıcının geçmiş regl sürelerini kullanarak ortalama regl uzunluğunu hesaplar.
     *
     * @return Ortalama regl süresi (gün olarak)
     */
    suspend operator fun invoke(): Int {
        val cycles = getMenstrualCyclesUseCase().first()

        // Hiç döngü yoksa hesaplama yapılamaz
        if (cycles.isEmpty()) {
            return 5 // Varsayılan değer
        }

        // Bitiş tarihi tanımlı olan döngüleri filtrele
        val completeCycles = cycles.filter { it.endDate != null }

        if (completeCycles.isEmpty()) {
            return 5 // Varsayılan değer
        }

        // Her döngü için regl süresini hesapla
        val periodLengths = completeCycles.map { cycle ->
            ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate).toInt() + 1
        }

        // Ortalama regl süresini hesapla
        return periodLengths.sum() / periodLengths.size
    }
}