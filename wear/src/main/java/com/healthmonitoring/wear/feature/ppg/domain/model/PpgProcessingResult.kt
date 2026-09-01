package com.healthmonitoring.wear.feature.ppg.domain.model

data class PpgProcessingResult(
    val measurement: PpgProcessedSample,
    val peak: PpgPeak? = null,
    val heartRate: PpgHeartRate? = null
)