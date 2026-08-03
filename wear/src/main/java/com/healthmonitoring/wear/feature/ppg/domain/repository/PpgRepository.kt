package com.healthmonitoring.wear.feature.ppg.domain.repository

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import kotlinx.coroutines.flow.Flow

interface PpgRepository {
    fun observePpg(): Flow<PpgMeasurement>

    fun observeMeasurementErrors(): Flow<String>

    fun startMeasurement()

    fun stopMeasurement()
}