package com.healthmonitoring.mobile.feature.skin_temperature.presentation

data class SkinTemperatureState(
    val objectTemperature: Float? = null,
    val ambientTemperature: Float? = null,
    val status: Int? = null,
    val timestamp: Long? = null
)