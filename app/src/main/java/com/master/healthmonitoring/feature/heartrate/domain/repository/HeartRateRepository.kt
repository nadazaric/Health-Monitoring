package com.master.healthmonitoring.feature.heartrate.domain.repository

import com.master.healthmonitoring.feature.heartrate.domain.model.HeartRateMeasurement
import kotlinx.coroutines.flow.Flow

interface HeartRateRepository {

    fun observeHeartRate(): Flow<HeartRateMeasurement>

    fun startTracking()

    fun stopTracking()

}