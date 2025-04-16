package com.kcguran.menstrualtracking.domain.usecase.menstrualcycle

import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import com.kcguran.menstrualtracking.domain.repository.MenstrualCycleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetMenstrualCyclesUseCase @Inject constructor(
    private val menstrualCycleRepository: MenstrualCycleRepository
) {
    operator fun invoke(): Flow<List<MenstrualCycle>> {
        return menstrualCycleRepository.getAllMenstrualCycles()
            .map { cycles ->
                cycles.sortedByDescending { it.startDate }
            }
    }

    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<MenstrualCycle>> {
        return menstrualCycleRepository.getMenstrualCyclesByDateRange(startDate, endDate)
            .map { cycles ->
                cycles.sortedByDescending { it.startDate }
            }
    }

    fun getLastCycle(): Flow<MenstrualCycle?> {
        return menstrualCycleRepository.getAllMenstrualCycles()
            .map { cycles ->
                cycles.maxByOrNull { it.startDate }
            }
    }
}