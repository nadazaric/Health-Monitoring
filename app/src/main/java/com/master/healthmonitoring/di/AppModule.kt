package com.master.healthmonitoring.di

import com.master.healthmonitoring.core.HealthTrackerProvider
import com.master.healthmonitoring.core.HealthTrackerProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HealthTrackingModule {

    @Binds
    @Singleton
    abstract fun bindHealthTrackerProvider(
        healthTrackerProviderImpl: HealthTrackerProviderImpl
    ): HealthTrackerProvider

}