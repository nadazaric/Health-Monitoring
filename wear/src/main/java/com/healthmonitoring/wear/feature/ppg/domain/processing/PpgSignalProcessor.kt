package com.healthmonitoring.wear.feature.ppg.domain.processing

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgRawSample
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessingResult

interface PpgSignalProcessor {

    fun process(measurement: PpgRawSample): List<PpgProcessingResult>

    fun reset()

    fun resetMeasurementState()
}