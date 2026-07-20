package com.healthmonitoring.wear.core.datalayer

import com.healthmonitoring.wear.feature.heartrate.domain.model.HeartRateMeasurement
import com.healthmonitoring.wear.feature.skin_temperature.domain.model.SkinTemperatureMeasurement

interface HealthDataMessageSender {
    fun sendHeartRateMeasurement(measurement: HeartRateMeasurement)
    fun sendSkinTemperatureMeasurement(measurement: SkinTemperatureMeasurement)
}