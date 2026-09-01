package com.healthmonitoring.wear.feature.ppg.domain.repository

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgRawSample
import kotlinx.coroutines.flow.Flow

interface PpgRepository {
    fun observePpg(): Flow<PpgRawSample>

    fun observeMeasurementErrors(): Flow<String>

    fun startMeasurement()

    fun stopMeasurement()
}