package com.healthmonitoring.mobile.feature.heartrate.domain.repository

import com.healthmonitoring.mobile.feature.heartrate.domain.model.HeartRateMeasurement
import kotlinx.coroutines.flow.Flow

interface HeartRateRepository {
    fun observeHeartRate(): Flow<HeartRateMeasurement>
}