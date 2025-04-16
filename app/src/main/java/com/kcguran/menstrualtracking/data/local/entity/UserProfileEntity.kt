package com.kcguran.menstrualtracking.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kcguran.menstrualtracking.domain.model.UserProfile

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Long = 1,
    val name: String,
    val age: Int,
    val averageCycleLength: Int,
    val averagePeriodLength: Int,
    val notificationsEnabled: Boolean
) {
    fun toDomainModel(): UserProfile {
        return UserProfile(
            id = id,
            name = name,
            age = age,
            averageCycleLength = averageCycleLength,
            averagePeriodLength = averagePeriodLength,
            notificationsEnabled = notificationsEnabled
        )
    }

    companion object {
        fun fromDomainModel(domainModel: UserProfile): UserProfileEntity {
            return UserProfileEntity(
                id = domainModel.id,
                name = domainModel.name,
                age = domainModel.age,
                averageCycleLength = domainModel.averageCycleLength,
                averagePeriodLength = domainModel.averagePeriodLength,
                notificationsEnabled = domainModel.notificationsEnabled
            )
        }
    }
}