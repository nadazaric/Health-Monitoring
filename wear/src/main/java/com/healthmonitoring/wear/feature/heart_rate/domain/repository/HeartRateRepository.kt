package com.healthmonitoring.wear.feature.heart_rate.domain.repository

import com.healthmonitoring.wear.feature.heart_rate.domain.model.HeartRateMeasurement
import kotlinx.coroutines.flow.Flow

interface HeartRateRepository {

    fun observeHeartRate(): Flow<HeartRateMeasurement>

    fun startTracking()

    fun stopTracking()

}