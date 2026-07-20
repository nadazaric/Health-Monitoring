package com.healthmonitoring.mobile.feature.spo2.domain.model

data class SpO2MeasurementStateUpdate(
    val measurementState: SpO2MeasurementState,
    val errorMessage: String?,
    val updatedAt: Long
)