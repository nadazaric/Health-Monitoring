package com.healthmonitoring.mobile.core.datalayer.model

data class HeartRateMeasurement(
    val bpm: Int,
    val status: Int,
    val timestamp: Long
)