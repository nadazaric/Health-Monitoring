package com.healthmonitoring.mobile.di

import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiverImpl
import com.healthmonitoring.mobile.feature.heartrate.data.repository.HeartRateRepositoryImpl
import com.healthmonitoring.mobile.feature.heartrate.domain.repository.HeartRateRepository
import com.healthmonitoring.mobile.feature.skin_temperature.data.repository.SkinTemperatureRepositoryImpl
import com.healthmonitoring.mobile.feature.skin_temperature.domain.repository.SkinTemperatureRepository
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

    @Binds
    @Singleton
    abstract fun bindSkinTemperatureRepository(
        skinTemperatureRepositoryImpl: SkinTemperatureRepositoryImpl
    ): SkinTemperatureRepository

    // Communication
    @Binds
    @Singleton
    abstract fun bindHealthDataReceiver(
        healthDataReceiverImpl: HealthDataReceiverImpl
    ): HealthDataReceiver

}