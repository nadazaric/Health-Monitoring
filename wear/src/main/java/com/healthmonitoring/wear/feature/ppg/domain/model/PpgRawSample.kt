package com.healthmonitoring.wear.feature.ppg.domain.model

data class PpgRawSample(
    val green: Int,
    val red: Int,
    val infrared: Int,
    val greenStatus: Int,
    val redStatus: Int,
    val infraredStatus: Int,
    val timestamp: Long
)