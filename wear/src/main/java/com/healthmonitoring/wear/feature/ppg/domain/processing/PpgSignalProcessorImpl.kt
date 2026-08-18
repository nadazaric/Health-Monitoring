package com.healthmonitoring.wear.feature.ppg.domain.processing

import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgChannelSubtraction
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgDcRemovalType
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgHeartRate
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgPeak
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessingResult
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

    // Peak detection
    private var previousPreviousProcessedSample: PpgProcessedMeasurement? = null
    private var previousProcessedSample: PpgProcessedMeasurement? = null
    private val detectedPeaks = mutableListOf<PpgPeak>()

    override fun process(measurement: PpgMeasurement): List<PpgProcessingResult> {
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

            val processedMeasurement = PpgProcessedMeasurement(
                value = invertSignal(filteredValue),
                timestamp = sample.timestamp
            )

            val peak = detectPeak(processedMeasurement)

            PpgProcessingResult(
                measurement = processedMeasurement,
                peak = peak,
                heartRate = if (peak != null) calculateHeartRate() else null
            )
        }
    }

    override fun reset() {
        resetCausalWindow()
        resetCenteredWindow()
        ppgSignalFilter.reset()
        resetPeaks()
    }

    override fun resetMeasurementState() {
        resetPeaks()
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

    private fun detectPeak(sample: PpgProcessedMeasurement): PpgPeak? {
        if (!PpgConfig.PEAK_DETECTION_ENABLED) {
            return null
        }

        val previousPreviousSample = previousPreviousProcessedSample
        val previousSample = previousProcessedSample

        previousPreviousProcessedSample = previousSample
        previousProcessedSample = sample

        if (previousPreviousSample == null || previousSample == null) {
            return null
        }

        val isLocalPeak =
            previousSample.value > previousPreviousSample.value &&
                    previousSample.value >= sample.value

        if (!isLocalPeak || previousSample.value < PpgConfig.PEAK_MIN_HEIGHT) {
            return null
        }

        val peak = PpgPeak(
            value = previousSample.value,
            timestamp = previousSample.timestamp
        )

        if (detectedPeaks.isEmpty()) {
            detectedPeaks.add(peak)
            return peak
        }

        val previousPeak = detectedPeaks.last()
        val distanceFromPreviousPeak = peak.timestamp - previousPeak.timestamp

        if (distanceFromPreviousPeak >= PpgConfig.PEAK_MIN_DISTANCE_MILLIS) {
            detectedPeaks.add(peak)
            return peak
        }

        if (peak.value > previousPeak.value) {
            detectedPeaks[detectedPeaks.lastIndex] = peak
            return peak
        }

        return null
    }

    private fun resetPeaks() {
        previousPreviousProcessedSample = null
        previousProcessedSample = null
        detectedPeaks.clear()
    }

    private fun calculateHeartRate(): PpgHeartRate? {
        if (!PpgConfig.HEART_RATE_ENABLED) {
            return null
        }

        if (detectedPeaks.size < 2) {
            return PpgHeartRate(
                currentBpm = null,
                averageBpm = null,
                peakCount = detectedPeaks.size
            )
        }

        val bpmValues = mutableListOf<Double>()

        for (index in 1 until detectedPeaks.size) {
            val intervalMillis = detectedPeaks[index].timestamp - detectedPeaks[index - 1].timestamp

            if (intervalMillis <= 0) {
                continue
            }

            val intervalSeconds = intervalMillis / 1_000.0
            val bpm = 60.0 / intervalSeconds

            if (bpm in PpgConfig.HEART_RATE_MIN_BPM..PpgConfig.HEART_RATE_MAX_BPM) {
                bpmValues.add(bpm)
            }
        }

        if (bpmValues.isEmpty()) {
            return PpgHeartRate(
                currentBpm = null,
                averageBpm = null,
                peakCount = detectedPeaks.size
            )
        }

        val averagingCount = maxOf(1, PpgConfig.HEART_RATE_AVERAGING_INTERVAL_COUNT)

        val recentBpmValues = bpmValues.takeLast(averagingCount)

        return PpgHeartRate(
            currentBpm = recentBpmValues.average(),
            averageBpm = bpmValues.average(),
            peakCount = detectedPeaks.size
        )
    }

    private fun invertSignal(value: Double): Double {
        return if (PpgConfig.INVERT_PROCESSED_SIGNAL) {
            -value
        } else {
            value
        }
    }
}