package com.healthmonitoring.mobile.feature.spo2.presentation

import com.healthmonitoring.mobile.feature.spo2.domain.model.SpO2MeasurementState

data class SpO2State(
    val spo2: Int? = null,
    val heartRate: Int? = null,
    val status: Int? = null,
    val timestamp: Long? = null,
    val measurementState: SpO2MeasurementState = SpO2MeasurementState.IDLE,
    val errorMessage: String? = null
) {
    val isMeasuring: Boolean
        get() = measurementState == SpO2MeasurementState.MEASURING
}