package com.healthmonitoring.mobile.core.datalayer

import com.healthmonitoring.mobile.feature.heartrate.domain.model.HeartRateMeasurement
import com.healthmonitoring.mobile.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import kotlinx.coroutines.flow.Flow

interface HealthDataReceiver {
    fun observeHeartRate(): Flow<HeartRateMeasurement>

    fun observeSkinTemperature(): Flow<SkinTemperatureMeasurement>

    fun startListening()

    fun stopListening()
}