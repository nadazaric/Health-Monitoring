package com.healthmonitoring.wear.feature.ppg.domain.model

data class PpgMeasurementSession(
    val id: String,
    val startedAt: Long,
    val endedAt: Long,
    val samples: List<PpgSessionSample>
)