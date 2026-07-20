package com.healthmonitoring.wear.feature.spo2.domain.model

data class SpO2Measurement(
    val spo2: Int,
    val heartRate: Int,
    val status: Int,
    val timestamp: Long
)