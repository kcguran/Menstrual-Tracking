package com.kcguran.menstrualtracking.di

import com.kcguran.menstrualtracking.data.repository.MenstrualCycleRepositoryImpl
import com.kcguran.menstrualtracking.data.repository.UserProfileRepositoryImpl
import com.kcguran.menstrualtracking.domain.repository.MenstrualCycleRepository
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindMenstrualCycleRepository(
        repository: MenstrualCycleRepositoryImpl
    ): MenstrualCycleRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(
        repository: UserProfileRepositoryImpl
    ): UserProfileRepository
}