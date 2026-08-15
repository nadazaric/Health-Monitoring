package com.healthmonitoring.wear.feature.ppg.domain.processing

import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgChannelSubtraction
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedMeasurement
import javax.inject.Inject

class PpgSignalProcessorImpl @Inject constructor() :
    PpgSignalProcessor {

    override fun process(measurement: PpgMeasurement): PpgProcessedMeasurement {
        val processedValue = calculateChannelSubtraction(measurement = measurement)

        return PpgProcessedMeasurement(
            value = processedValue,
            timestamp = measurement.timestamp
        )
    }

    override fun reset() {
    }

    private fun calculateChannelSubtraction(measurement: PpgMeasurement): Double {
        return when (PpgConfig.CHANNEL_SUBTRACTION) {
            PpgChannelSubtraction.NONE -> {
                measurement.green.toDouble()
            }

            PpgChannelSubtraction.RED -> {
                measurement.green.toDouble() - measurement.red.toDouble()
            }

            PpgChannelSubtraction.INFRARED -> {
                measurement.green.toDouble() - measurement.infrared.toDouble()
            }

            PpgChannelSubtraction.RED_INFRARED_MEAN -> {
                measurement.green.toDouble() -
                        (measurement.red.toDouble() + measurement.infrared.toDouble()) / 2.0
            }
        }
    }
}