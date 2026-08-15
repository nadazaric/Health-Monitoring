package com.healthmonitoring.wear.feature.ppg.domain.processing

import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgChannelSubtraction
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgDcRemovalType
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedMeasurement
import javax.inject.Inject

class PpgSignalProcessorImpl @Inject constructor(
    private val ppgSignalFilter: PpgSignalFilter
) : PpgSignalProcessor {

    private data class SignalSample(
        val value: Double,
        val timestamp: Long
    )

    // DC Component removal
    private val causalWindow = ArrayDeque<SignalSample>()
    private var causalWindowSum = 0.0
    private val centeredSamples = mutableListOf<SignalSample>()
    private var centeredStartIndex = 0
    private var centeredEndIndex = 0
    private var centeredCurrentIndex = 0
    private var centeredWindowSum = 0.0

    override fun process(measurement: PpgMeasurement): List<PpgProcessedMeasurement> {
        val channelSubtractedSample = SignalSample(
            value = calculateChannelSubtraction(measurement),
            timestamp = measurement.timestamp
        )

        val dcRemovedSamples = removeDcComponent(
            sample = channelSubtractedSample
        )

        return dcRemovedSamples.map { sample ->
            val filteredValue = ppgSignalFilter.apply(
                value = sample.value,
                timestamp = sample.timestamp
            )

            PpgProcessedMeasurement(
                value = invertSignal(filteredValue),
                timestamp = sample.timestamp
            )
        }
    }

    override fun reset() {
        resetCausalWindow()
        resetCenteredWindow()
        ppgSignalFilter.reset()
    }

    private fun calculateChannelSubtraction(measurement: PpgMeasurement): Double {
        return when (PpgConfig.CHANNEL_SUBTRACTION) {
            PpgChannelSubtraction.NONE ->
                measurement.green.toDouble()

            PpgChannelSubtraction.RED ->
                measurement.green.toDouble() - measurement.red.toDouble()

            PpgChannelSubtraction.INFRARED ->
                measurement.green.toDouble() - measurement.infrared.toDouble()

            PpgChannelSubtraction.RED_INFRARED_MEAN ->
                measurement.green.toDouble() -
                        (measurement.red.toDouble() + measurement.infrared.toDouble()) / 2.0
        }
    }

    private fun removeDcComponent(sample: SignalSample): List<SignalSample> {
        return when (PpgConfig.DC_REMOVAL_TYPE) {
            PpgDcRemovalType.NONE -> listOf(sample)

            PpgDcRemovalType.CAUSAL ->
                listOf(subtractCausalMovingAverage(sample))

            PpgDcRemovalType.CENTERED ->
                subtractCenteredMovingAverage(sample)
        }
    }

    private fun subtractCausalMovingAverage(sample: SignalSample): SignalSample {
        causalWindow.addLast(sample)
        causalWindowSum += sample.value

        while (
            causalWindow.size > 1 &&
            sample.timestamp - causalWindow.first().timestamp >
            PpgConfig.DC_REMOVAL_WINDOW_MILLIS
        ) {
            causalWindowSum -= causalWindow.removeFirst().value
        }

        val baseline = causalWindowSum / causalWindow.size

        return SignalSample(
            value = sample.value - baseline,
            timestamp = sample.timestamp
        )
    }

    private fun subtractCenteredMovingAverage(sample: SignalSample): List<SignalSample> {
        centeredSamples.add(sample)

        val result = mutableListOf<SignalSample>()
        val halfWindowMillis =
            PpgConfig.DC_REMOVAL_WINDOW_MILLIS / 2.0
        val lastTimestamp = centeredSamples.last().timestamp

        while (centeredCurrentIndex < centeredSamples.size) {
            val currentSample =
                centeredSamples[centeredCurrentIndex]

            if (lastTimestamp - currentSample.timestamp < halfWindowMillis) {
                break
            }

            while (
                centeredStartIndex < centeredCurrentIndex &&
                currentSample.timestamp - centeredSamples[centeredStartIndex].timestamp > halfWindowMillis
            ) {
                centeredWindowSum -= centeredSamples[centeredStartIndex].value
                centeredStartIndex++
            }

            while (
                centeredEndIndex < centeredSamples.size &&
                centeredSamples[centeredEndIndex].timestamp - currentSample.timestamp <= halfWindowMillis
            ) {
                centeredWindowSum += centeredSamples[centeredEndIndex].value
                centeredEndIndex++
            }

            val windowCount = centeredEndIndex - centeredStartIndex

            if (windowCount > 0) {
                val baseline = centeredWindowSum / windowCount

                result.add(
                    SignalSample(
                        value = currentSample.value - baseline,
                        timestamp = currentSample.timestamp
                    )
                )
            }

            centeredCurrentIndex++
        }

        return result
    }

    private fun resetCausalWindow() {
        causalWindow.clear()
        causalWindowSum = 0.0
    }

    private fun resetCenteredWindow() {
        centeredSamples.clear()
        centeredStartIndex = 0
        centeredEndIndex = 0
        centeredCurrentIndex = 0
        centeredWindowSum = 0.0
    }

    private fun invertSignal(value: Double): Double {
        return if (PpgConfig.INVERT_PROCESSED_SIGNAL) {
            -value
        } else {
            value
        }
    }
}