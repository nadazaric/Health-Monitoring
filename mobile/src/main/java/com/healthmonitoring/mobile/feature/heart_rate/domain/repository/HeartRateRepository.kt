package com.healthmonitoring.mobile.feature.heart_rate.domain.repository

import com.healthmonitoring.mobile.feature.heart_rate.domain.model.HeartRateMeasurement
import kotlinx.coroutines.flow.Flow

interface HeartRateRepository {
    fun observeHeartRate(): Flow<HeartRateMeasurement>
}