package com.healthmonitoring.wear.feature.ppg.presentation

import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgHeartRate
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgPeak
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedSample

data class PpgState(
    val chartMeasurements: List<PpgProcessedSample> = emptyList(),
    val remainingTimeMillis: Long = PpgConfig.MEASUREMENT_DURATION_MILLIS,
    val measurementPhase: PpgMeasurementPhase = PpgMeasurementPhase.IDLE,
    val errorMessage: String? = null,
    val chartPeaks: List<PpgPeak> = emptyList(),
    val heartRate: PpgHeartRate? = null,
    val breathingPhase: PpgBreathingPhase? = null,
) {
    val isMeasurementActive: Boolean
        get() = measurementPhase == PpgMeasurementPhase.STARTUP_TRIM ||
                measurementPhase == PpgMeasurementPhase.PROCESSING_WARMUP ||
                measurementPhase == PpgMeasurementPhase.MEASURING

    val isPreparing: Boolean
        get() = measurementPhase == PpgMeasurementPhase.STARTUP_TRIM ||
                measurementPhase == PpgMeasurementPhase.PROCESSING_WARMUP

    val isMeasuring: Boolean
        get() = measurementPhase == PpgMeasurementPhase.MEASURING

    val isMeasurementCompleted: Boolean
        get() = measurementPhase == PpgMeasurementPhase.COMPLETED
}