package com.healthmonitoring.mobile.feature.heart_rate.domain.model

data class HeartRateMeasurement(
    val bpm: Int,
    val status: Int,
    val timestamp: Long
)