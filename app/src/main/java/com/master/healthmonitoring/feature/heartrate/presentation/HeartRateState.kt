package com.master.healthmonitoring.feature.heartrate.presentation

data class HeartRateState(
    val bpm: Int? = null,
    val status: Int? = null,
    val timestamp: Long? = null,
    val isTracking: Boolean = false,
    val errorMessage: String? = null
)