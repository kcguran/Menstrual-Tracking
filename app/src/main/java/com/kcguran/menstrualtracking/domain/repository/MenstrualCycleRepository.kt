package com.kcguran.menstrualtracking.domain.repository

import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MenstrualCycleRepository {
    suspend fun addMenstrualCycle(menstrualCycle: MenstrualCycle): Long
    suspend fun updateMenstrualCycle(menstrualCycle: MenstrualCycle)
    suspend fun getMenstrualCycleById(id: Long): MenstrualCycle?
    fun getAllMenstrualCycles(): Flow<List<MenstrualCycle>>
    fun getMenstrualCyclesByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<MenstrualCycle>>
    suspend fun deleteMenstrualCycle(id: Long)
}