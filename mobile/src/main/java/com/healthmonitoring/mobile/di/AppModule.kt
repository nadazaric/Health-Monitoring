package com.healthmonitoring.mobile.di

import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiverImpl
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
    abstract fun bindHealthDataReceiver(
        healthDataReceiverImpl: HealthDataReceiverImpl
    ): HealthDataReceiver

}