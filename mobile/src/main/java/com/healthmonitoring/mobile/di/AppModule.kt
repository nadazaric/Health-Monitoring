package com.healthmonitoring.mobile.di

import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiverImpl
import com.healthmonitoring.mobile.feature.heartrate.data.repository.HeartRateRepositoryImpl
import com.healthmonitoring.mobile.feature.heartrate.domain.repository.HeartRateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    // Repositories
    @Binds
    @Singleton
    abstract fun bindHeartRateRepository(
        heartRateRepositoryImpl: HeartRateRepositoryImpl
    ): HeartRateRepository

    // Communication
    @Binds
    @Singleton
    abstract fun bindHealthDataReceiver(
        healthDataReceiverImpl: HealthDataReceiverImpl
    ): HealthDataReceiver

}