package com.healthmonitoring.wear.feature.ppg.data.session

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurementSession
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedSample
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgRawSample
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgSessionSample

interface PpgSessionCollector {

    fun start(startedAt: Long)

    fun addRawSample(sample: PpgRawSample)

    fun addProcessedSample(sample: PpgProcessedSample): PpgSessionSample?

    fun finish(endedAt: Long): PpgMeasurementSession

    fun reset()
}