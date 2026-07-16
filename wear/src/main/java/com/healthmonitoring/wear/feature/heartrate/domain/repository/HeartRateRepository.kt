package com.healthmonitoring.wear.feature.heartrate.domain.repository

import com.healthmonitoring.wear.feature.heartrate.domain.model.HeartRateMeasurement
import kotlinx.coroutines.flow.Flow

interface HeartRateRepository {

    fun observeHeartRate(): Flow<HeartRateMeasurement>

    fun startTracking()

    fun stopTracking()

}