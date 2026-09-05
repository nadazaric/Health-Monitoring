package com.healthmonitoring.mobile.di

import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiverImpl
import com.healthmonitoring.mobile.core.datalayer.PpgSessionReceiver
import com.healthmonitoring.mobile.core.datalayer.PpgSessionReceiverImpl
import com.healthmonitoring.mobile.feature.heart_rate.data.repository.HeartRateRepositoryImpl
import com.healthmonitoring.mobile.feature.heart_rate.domain.repository.HeartRateRepository
import com.healthmonitoring.mobile.feature.skin_temperature.data.repository.SkinTemperatureRepositoryImpl
import com.healthmonitoring.mobile.feature.skin_temperature.domain.repository.SkinTemperatureRepository
import com.healthmonitoring.mobile.feature.spo2.data.repository.SpO2RepositoryImpl
import com.healthmonitoring.mobile.feature.spo2.domain.repository.SpO2Repository
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

    @Binds
    @Singleton
    abstract fun bindSpO2Repository(
        spO2RepositoryImpl: SpO2RepositoryImpl
    ): SpO2Repository

    // Communication
    @Binds
    @Singleton
    abstract fun bindHealthDataReceiver(
        healthDataReceiverImpl: HealthDataReceiverImpl
    ): HealthDataReceiver

    @Binds
    @Singleton
    abstract fun bindPpgSessionReceiver(
        ppgSessionReceiverImpl: PpgSessionReceiverImpl
    ): PpgSessionReceiver

}