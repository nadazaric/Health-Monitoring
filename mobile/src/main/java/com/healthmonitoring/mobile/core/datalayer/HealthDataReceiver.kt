package com.healthmonitoring.mobile.core.datalayer

import com.healthmonitoring.mobile.feature.heart_rate.domain.model.HeartRateMeasurement
import com.healthmonitoring.mobile.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.healthmonitoring.mobile.feature.spo2.domain.model.SpO2Measurement
import kotlinx.coroutines.flow.Flow

interface HealthDataReceiver {
    fun observeHeartRate(): Flow<HeartRateMeasurement>

    fun observeSkinTemperature(): Flow<SkinTemperatureMeasurement>

    fun observeSpO2(): Flow<SpO2Measurement>

    fun startListening()

    fun stopListening()
}