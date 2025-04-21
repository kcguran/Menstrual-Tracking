package com.kcguran.menstrualtracking.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kcguran.menstrualtracking.data.local.converter.DateConverter
import com.kcguran.menstrualtracking.data.local.converter.StringListConverter
import com.kcguran.menstrualtracking.data.local.dao.MenstrualCycleDao
import com.kcguran.menstrualtracking.data.local.dao.UserProfileDao
import com.kcguran.menstrualtracking.data.local.entity.MenstrualCycleEntity
import com.kcguran.menstrualtracking.data.local.entity.UserProfileEntity

@Database(
    entities = [MenstrualCycleEntity::class, UserProfileEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(DateConverter::class, StringListConverter::class)
abstract class MenstrualDatabase : RoomDatabase() {
    abstract fun menstrualCycleDao(): MenstrualCycleDao
    abstract fun userProfileDao(): UserProfileDao
}