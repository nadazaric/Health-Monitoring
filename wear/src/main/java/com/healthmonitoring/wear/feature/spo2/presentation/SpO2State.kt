package com.healthmonitoring.wear.feature.spo2.presentation

data class SpO2State(
    val spo2: Int? = null,
    val heartRate: Int? = null,
    val status: Int? = null,
    val timestamp: Long? = null,
    val isMeasuring: Boolean = false,
    val errorMessage: String? = null
)