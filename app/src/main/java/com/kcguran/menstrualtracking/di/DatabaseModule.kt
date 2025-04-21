package com.kcguran.menstrualtracking.di

import android.content.Context
import androidx.room.Room
import com.kcguran.menstrualtracking.data.local.MenstrualDatabase
import com.kcguran.menstrualtracking.data.local.dao.MenstrualCycleDao
import com.kcguran.menstrualtracking.data.local.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MenstrualDatabase {
        return Room.databaseBuilder(
            context,
            MenstrualDatabase::class.java,
            "menstrual_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMenstrualCycleDao(database: MenstrualDatabase): MenstrualCycleDao {
        return database.menstrualCycleDao()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(database: MenstrualDatabase): UserProfileDao {
        return database.userProfileDao()
    }
}