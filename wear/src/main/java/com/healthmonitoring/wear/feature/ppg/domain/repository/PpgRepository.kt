package com.healthmonitoring.wear.feature.ppg.domain.repository

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurementSession
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedSample
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgRawSample
import kotlinx.coroutines.flow.Flow

interface PpgRepository {

    fun observePpg(): Flow<PpgRawSample>

    fun observeMeasurementErrors(): Flow<String>

    fun startMeasurement()

    fun stopMeasurement()

    fun startSession(startedAt: Long)

    fun addProcessedSampleToSession(sample: PpgProcessedSample)

    fun resetSession()

    fun finishSession(endedAt: Long): PpgMeasurementSession
}