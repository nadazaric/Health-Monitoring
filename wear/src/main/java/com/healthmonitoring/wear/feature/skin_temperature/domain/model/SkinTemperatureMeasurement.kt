package com.healthmonitoring.wear.feature.skin_temperature.domain.model

data class SkinTemperatureMeasurement(
    val objectTemperature: Float,
    val ambientTemperature: Float,
    val status: Int,
    val timestamp: Long
)