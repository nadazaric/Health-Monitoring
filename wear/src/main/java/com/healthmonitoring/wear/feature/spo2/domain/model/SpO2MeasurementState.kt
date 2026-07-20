package com.healthmonitoring.wear.feature.spo2.domain.model

enum class SpO2MeasurementState(
    val dataLayerValue: String
) {
    IDLE("idle"),
    MEASURING("measuring"),
    COMPLETED("completed"),
    FAILED("failed");

    companion object {
        fun fromDataLayerValue(value: String?): SpO2MeasurementState {
            return entries.firstOrNull { state ->
                state.dataLayerValue == value
            } ?: IDLE
        }
    }
}