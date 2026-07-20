package com.healthmonitoring.mobile.feature.heart_rate.data.repository

import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.feature.heart_rate.domain.model.HeartRateMeasurement
import com.healthmonitoring.mobile.feature.heart_rate.domain.repository.HeartRateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeartRateRepositoryImpl @Inject constructor(
    private val healthDataReceiver: HealthDataReceiver
) : HeartRateRepository {

    override fun observeHeartRate(): Flow<HeartRateMeasurement> {
        return healthDataReceiver.observeHeartRate()
    }
}