package com.healthmonitoring.wear.feature.spo2.domain.repository

import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2Measurement
import kotlinx.coroutines.flow.Flow

interface SpO2Repository {
    fun observeSpO2(): Flow<SpO2Measurement>

    fun observeMeasurementErrors(): Flow<String>

    fun startMeasurement()

    fun stopMeasurement()
}