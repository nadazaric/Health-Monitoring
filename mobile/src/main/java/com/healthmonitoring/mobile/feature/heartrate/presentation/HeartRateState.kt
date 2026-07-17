package com.healthmonitoring.mobile.feature.heartrate.presentation

data class HeartRateState(
    val bpm: Int? = null,
    val status: Int? = null,
    val timestamp: Long? = null
)