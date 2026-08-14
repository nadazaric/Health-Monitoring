package com.healthmonitoring.wear.feature.ppg.presentation

import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement

data class PpgState(
    val measurement: PpgMeasurement? = null,
    val remainingTimeMillis: Long = PpgConfig.MEASUREMENT_DURATION_MILLIS,
    val isMeasuring: Boolean = false,
    val isMeasurementCompleted: Boolean = false,
    val errorMessage: String? = null
)