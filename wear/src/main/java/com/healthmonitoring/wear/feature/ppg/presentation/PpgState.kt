package com.healthmonitoring.wear.feature.ppg.presentation

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement

data class PpgState(
    val measurement: PpgMeasurement? = null,
    val isMeasuring: Boolean = false,
    val errorMessage: String? = null
)