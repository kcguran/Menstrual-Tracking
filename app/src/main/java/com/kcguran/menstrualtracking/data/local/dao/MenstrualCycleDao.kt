package com.kcguran.menstrualtracking.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kcguran.menstrualtracking.data.local.entity.MenstrualCycleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MenstrualCycleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenstrualCycle(menstrualCycle: MenstrualCycleEntity): Long
    @Update
    suspend fun updateMenstrualCycle(menstrualCycle: MenstrualCycleEntity)

    @Query("SELECT * FROM menstrual_cycles WHERE id = :id")
    suspend fun getMenstrualCycleById(id: Long): MenstrualCycleEntity?

    @Query("SELECT * FROM menstrual_cycles ORDER BY startDate DESC")
    fun getAllMenstrualCycles(): Flow<List<MenstrualCycleEntity>>

    @Query("SELECT * FROM menstrual_cycles WHERE startDate BETWEEN :startDate AND :endDate OR endDate BETWEEN :startDate AND :endDate ORDER BY startDate DESC")
    fun getMenstrualCyclesByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<MenstrualCycleEntity>>

    @Query("DELETE FROM menstrual_cycles WHERE id = :id")
    suspend fun deleteMenstrualCycle(id: Long)

    @Query("DELETE FROM menstrual_cycles")
    suspend fun deleteAllMenstrualCycles()
}