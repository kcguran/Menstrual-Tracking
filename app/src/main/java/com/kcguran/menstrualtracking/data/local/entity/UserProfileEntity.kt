// data/local/entity/UserProfileEntity.kt
package com.kcguran.menstrualtracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kcguran.menstrualtracking.data.local.converter.DateConverter
import com.kcguran.menstrualtracking.data.local.converter.StringListConverter
import com.kcguran.menstrualtracking.domain.model.UserProfile
import java.time.LocalDate

@Entity(tableName = "user_profile")
@TypeConverters(DateConverter::class, StringListConverter::class)
data class UserProfileEntity(
    @PrimaryKey
    val id: Long = 1,
    val name: String,
    val surname: String,
    val age: Int,
    val height: Int,
    val weight: Int,
    val averageCycleLength: Int,
    val averagePeriodLength: Int,
    val notificationsEnabled: Boolean,
    val commonSymptoms: List<String>,
    val lastPeriodStartDate: LocalDate?,
    val isFirstLogin: Boolean
) {
    fun toDomainModel(): UserProfile {
        return UserProfile(
            id = id,
            name = name,
            surname = surname,
            age = age,
            height = height,
            weight = weight,
            averageCycleLength = averageCycleLength,
            averagePeriodLength = averagePeriodLength,
            notificationsEnabled = notificationsEnabled,
            commonSymptoms = commonSymptoms,
            lastPeriodStartDate = lastPeriodStartDate,
            isFirstLogin = isFirstLogin
        )
    }

    companion object {
        fun fromDomainModel(domainModel: UserProfile): UserProfileEntity {
            return UserProfileEntity(
                id = domainModel.id,
                name = domainModel.name,
                surname = domainModel.surname,
                age = domainModel.age,
                height = domainModel.height,
                weight = domainModel.weight,
                averageCycleLength = domainModel.averageCycleLength,
                averagePeriodLength = domainModel.averagePeriodLength,
                notificationsEnabled = domainModel.notificationsEnabled,
                commonSymptoms = domainModel.commonSymptoms,
                lastPeriodStartDate = domainModel.lastPeriodStartDate,
                isFirstLogin = domainModel.isFirstLogin
            )
        }
    }
}