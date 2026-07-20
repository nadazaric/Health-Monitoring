package com.healthmonitoring.mobile.feature.heart_rate.presentation

data class HeartRateState(
    val bpm: Int? = null,
    val status: Int? = null,
    val timestamp: Long? = null
)