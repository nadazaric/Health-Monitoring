package com.healthmonitoring.wear.feature.ppg.domain.processing

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedMeasurement

interface PpgSignalProcessor {

    fun process(measurement: PpgMeasurement): List<PpgProcessedMeasurement>

    fun reset()
}