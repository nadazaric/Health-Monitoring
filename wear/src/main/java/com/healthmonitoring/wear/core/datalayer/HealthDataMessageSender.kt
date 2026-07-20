package com.healthmonitoring.wear.core.datalayer

import com.healthmonitoring.wear.feature.heart_rate.domain.model.HeartRateMeasurement
import com.healthmonitoring.wear.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2Measurement
import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2MeasurementState

interface HealthDataMessageSender {
    fun sendHeartRateMeasurement(measurement: HeartRateMeasurement)

    fun sendSkinTemperatureMeasurement(measurement: SkinTemperatureMeasurement)

    fun sendSpO2Measurement(measurement: SpO2Measurement)

    fun sendSpO2MeasurementState(
        measurementState: SpO2MeasurementState,
        errorMessage: String? = null
    )
}