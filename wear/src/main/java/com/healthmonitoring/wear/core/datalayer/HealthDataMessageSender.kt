package com.healthmonitoring.wear.core.datalayer

import com.healthmonitoring.wear.feature.heartrate.domain.model.HeartRateMeasurement

interface HealthDataMessageSender {
    fun sendHeartRateMeasurement(measurement: HeartRateMeasurement)
}