package com.healthmonitoring.wear.feature.ppg.domain.processing

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessingResult

interface PpgSignalProcessor {

    fun process(measurement: PpgMeasurement): List<PpgProcessingResult>

    fun reset()
}