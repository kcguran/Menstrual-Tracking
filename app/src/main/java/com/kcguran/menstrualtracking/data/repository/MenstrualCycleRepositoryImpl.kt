package com.kcguran.menstrualtracking.data.repository

import com.kcguran.menstrualtracking.data.local.dao.MenstrualCycleDao
import com.kcguran.menstrualtracking.data.local.entity.MenstrualCycleEntity
import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import com.kcguran.menstrualtracking.domain.repository.MenstrualCycleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class MenstrualCycleRepositoryImpl @Inject constructor(
    private val menstrualCycleDao: MenstrualCycleDao,
): MenstrualCycleRepository{

    override suspend fun addMenstrualCycle(menstrualCycle: MenstrualCycle): Long {
        return menstrualCycleDao.insertMenstrualCycle(MenstrualCycleEntity.fromDomainModel(menstrualCycle))
    }

    override suspend fun updateMenstrualCycle(menstrualCycle: MenstrualCycle) {
        menstrualCycleDao.updateMenstrualCycle(MenstrualCycleEntity.fromDomainModel(menstrualCycle))
    }

    override suspend fun getMenstrualCycleById(id: Long): MenstrualCycle? {
        return menstrualCycleDao.getMenstrualCycleById(id)?.toDomainModel()
    }

    override fun getAllMenstrualCycles(): Flow<List<MenstrualCycle>> {
        return menstrualCycleDao.getAllMenstrualCycles().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getMenstrualCyclesByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<MenstrualCycle>> {
        return menstrualCycleDao.getMenstrualCyclesByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun deleteMenstrualCycle(id: Long) {
        menstrualCycleDao.deleteMenstrualCycle(id)
    }

}
