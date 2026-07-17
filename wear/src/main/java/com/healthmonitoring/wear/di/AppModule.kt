package com.healthmonitoring.wear.di

import com.healthmonitoring.wear.core.HealthTrackerProvider
import com.healthmonitoring.wear.core.HealthTrackerProviderImpl
import com.healthmonitoring.wear.core.datalayer.HealthDataMessageSender
import com.healthmonitoring.wear.core.datalayer.HealthDataMessageSenderImpl
import com.healthmonitoring.wear.feature.heartrate.data.repository.HeartRateRepositoryImpl
import com.healthmonitoring.wear.feature.heartrate.domain.repository.HeartRateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HealthTrackingModule {

    // Trackers
    @Binds
    @Singleton
    abstract fun bindHealthTrackerProvider(
        healthTrackerProviderImpl: HealthTrackerProviderImpl
    ): HealthTrackerProvider

    // Repositories
    @Binds
    @Singleton
    abstract fun bindHeartRateRepository(
        heartRateRepositoryImpl: HeartRateRepositoryImpl
    ): HeartRateRepository

    // Communication
    @Binds
    @Singleton
    abstract fun bindHealthDataMessageSender(
        healthDataMessageSenderImpl: HealthDataMessageSenderImpl
    ): HealthDataMessageSender

}