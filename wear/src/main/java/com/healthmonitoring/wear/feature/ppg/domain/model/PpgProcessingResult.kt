package com.healthmonitoring.wear.feature.ppg.domain.model

data class PpgProcessingResult(
    val measurement: PpgProcessedMeasurement,
    val peak: PpgPeak? = null,
    val heartRate: PpgHeartRate? = null
)