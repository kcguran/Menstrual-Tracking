package com.kcguran.menstrualtracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kcguran.menstrualtracking.data.local.converter.DateConverter
import com.kcguran.menstrualtracking.data.local.converter.StringListConverter
import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "menstrual_cycles")
@TypeConverters(DateConverter::class, StringListConverter::class)
data class MenstrualCycleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val cycleLength: Int,
    val periodLength: Int,
    val symptoms: List<String>,
    val notes: String
) {
    fun toDomainModel(): MenstrualCycle {
        return MenstrualCycle(
            id = id,
            startDate = startDate,
            endDate = endDate,
            cycleLength = cycleLength,
            periodLength = periodLength,
            symptoms = symptoms,
            notes = notes
        )
    }

    companion object {
        fun fromDomainModel(domainModel: MenstrualCycle): MenstrualCycleEntity {
            return MenstrualCycleEntity(
                id = domainModel.id,
                startDate = domainModel.startDate,
                endDate = domainModel.endDate,
                cycleLength = domainModel.cycleLength,
                periodLength = domainModel.periodLength,
                symptoms = domainModel.symptoms,
                notes = domainModel.notes
            )
        }
    }
}