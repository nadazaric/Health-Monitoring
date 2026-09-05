package com.healthmonitoring.mobile.di

import android.content.Context
import androidx.room.Room
import com.healthmonitoring.mobile.core.database.HealthMonitoringDatabase
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiverImpl
import com.healthmonitoring.mobile.core.datalayer.PpgSessionReceiver
import com.healthmonitoring.mobile.core.datalayer.PpgSessionReceiverImpl
import com.healthmonitoring.mobile.feature.heart_rate.data.repository.HeartRateRepositoryImpl
import com.healthmonitoring.mobile.feature.heart_rate.domain.repository.HeartRateRepository
import com.healthmonitoring.mobile.feature.ppg.data.local.dao.PpgSessionDao
import com.healthmonitoring.mobile.feature.ppg.data.repository.PpgSessionRepositoryImpl
import com.healthmonitoring.mobile.feature.ppg.domain.repository.PpgSessionRepository
import com.healthmonitoring.mobile.feature.skin_temperature.data.repository.SkinTemperatureRepositoryImpl
import com.healthmonitoring.mobile.feature.skin_temperature.domain.repository.SkinTemperatureRepository
import com.healthmonitoring.mobile.feature.spo2.data.repository.SpO2RepositoryImpl
import com.healthmonitoring.mobile.feature.spo2.domain.repository.SpO2Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Binds
    @Singleton
    abstract fun bindPpgSessionRepository(
        ppgSessionRepositoryImpl: PpgSessionRepositoryImpl
    ): PpgSessionRepository

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

    // Database
    companion object {
        @Provides
        @Singleton
        fun provideHealthMonitoringDatabase(
            @ApplicationContext context: Context
        ): HealthMonitoringDatabase {
            return Room.databaseBuilder(
                context,
                HealthMonitoringDatabase::class.java,
                "health_monitoring_database"
            ).build()
        }

        @Provides
        fun providePpgSessionDao(
            database: HealthMonitoringDatabase
        ): PpgSessionDao {
            return database.ppgSessionDao()
        }
    }

}