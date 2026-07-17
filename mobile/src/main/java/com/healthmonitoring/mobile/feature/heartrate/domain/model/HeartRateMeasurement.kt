package com.healthmonitoring.mobile.feature.heartrate.domain.model

data class HeartRateMeasurement(
    val bpm: Int,
    val status: Int,
    val timestamp: Long
)