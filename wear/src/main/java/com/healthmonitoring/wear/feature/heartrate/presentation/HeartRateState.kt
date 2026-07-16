package com.healthmonitoring.wear.feature.heartrate.presentation

data class HeartRateState(
    val bpm: Int? = null,
    val status: Int? = null,
    val timestamp: Long? = null,
    val isTracking: Boolean = false,
    val errorMessage: String? = null
)