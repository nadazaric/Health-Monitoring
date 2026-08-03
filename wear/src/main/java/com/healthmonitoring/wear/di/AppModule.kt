package com.healthmonitoring.wear.di

import com.healthmonitoring.wear.core.HealthTrackerProvider
import com.healthmonitoring.wear.core.HealthTrackerProviderImpl
import com.healthmonitoring.wear.core.datalayer.HealthDataMessageSender
import com.healthmonitoring.wear.core.datalayer.HealthDataMessageSenderImpl
import com.healthmonitoring.wear.feature.heart_rate.data.repository.HeartRateRepositoryImpl
import com.healthmonitoring.wear.feature.heart_rate.domain.repository.HeartRateRepository
import com.healthmonitoring.wear.feature.ppg.data.repository.PpgRepositoryImpl
import com.healthmonitoring.wear.feature.ppg.domain.repository.PpgRepository
import com.healthmonitoring.wear.feature.skin_temperature.data.repository.SkinTemperatureRepositoryImpl
import com.healthmonitoring.wear.feature.skin_temperature.domain.repository.SkinTemperatureRepository
import com.healthmonitoring.wear.feature.spo2.data.repository.SpO2RepositoryImpl
import com.healthmonitoring.wear.feature.spo2.domain.repository.SpO2Repository
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

    @Binds
    @Singleton
    abstract fun bindSkinTemperatureRepository(
        skinTemperatureRepositoryImpl: SkinTemperatureRepositoryImpl
    ): SkinTemperatureRepository

    @Binds
    @Singleton
    abstract fun bindSpO2Repository(
        spO2RepositoryImpl: SpO2RepositoryImpl
    ): SpO2Repository

    @Binds
    @Singleton
    abstract fun bindPpgRepository(
        ppgRepositoryImpl: PpgRepositoryImpl
    ): PpgRepository

    // Communication
    @Binds
    @Singleton
    abstract fun bindHealthDataMessageSender(
        healthDataMessageSenderImpl: HealthDataMessageSenderImpl
    ): HealthDataMessageSender

}