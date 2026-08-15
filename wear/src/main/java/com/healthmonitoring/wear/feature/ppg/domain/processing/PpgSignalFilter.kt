package com.healthmonitoring.wear.feature.ppg.domain.processing

import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgFilterType
import javax.inject.Inject

class PpgSignalFilter @Inject constructor() {

    private data class HighPassFilterState(
        var previousInput: Double? = null,
        var previousOutput: Double = 0.0,
        var previousTimestamp: Long? = null
    )

    private data class LowPassFilterState(
        var previousOutput: Double? = null,
        var previousTimestamp: Long? = null
    )

    private val highPassFilterStates = mutableListOf<HighPassFilterState>()
    private val lowPassFilterStates = mutableListOf<LowPassFilterState>()

    fun apply(
        value: Double,
        timestamp: Long
    ): Double {
        return when (PpgConfig.FILTER_TYPE) {
            PpgFilterType.NONE -> value

            PpgFilterType.HIGH_PASS ->
                applyHighPassFilter(
                    value = value,
                    timestamp = timestamp
                )

            PpgFilterType.LOW_PASS ->
                applyLowPassFilter(
                    value = value,
                    timestamp = timestamp
                )

            PpgFilterType.BAND_PASS -> {
                val highPassValue = applyHighPassFilter(
                    value = value,
                    timestamp = timestamp
                )

                applyLowPassFilter(
                    value = highPassValue,
                    timestamp = timestamp
                )
            }
        }
    }

    fun reset() {
        highPassFilterStates.clear()
        lowPassFilterStates.clear()
    }

    private fun applyHighPassFilter(
        value: Double,
        timestamp: Long
    ): Double {
        val filterPasses = maxOf(1, PpgConfig.HIGH_PASS_FILTER_PASSES)

        while (lowPassFilterStates.size < filterPasses) {
            lowPassFilterStates.add(
                LowPassFilterState()
            )
        }

        var filteredValue = value

        highPassFilterStates.forEach { state ->
            val previousInput = state.previousInput
            val previousTimestamp = state.previousTimestamp

            if (previousInput == null || previousTimestamp == null) {
                state.previousInput = filteredValue
                state.previousOutput = 0.0
                state.previousTimestamp = timestamp

                filteredValue = 0.0
                return@forEach
            }

            val dt = maxOf(
                (timestamp - previousTimestamp) / 1_000.0,
                0.000001
            )

            val rc = 1.0 / (2.0 * Math.PI * PpgConfig.HIGH_PASS_CUTOFF_HZ)
            val alpha = rc / (rc + dt)

            val output = alpha * (
                    state.previousOutput +
                            filteredValue -
                            previousInput
                    )

            state.previousInput = filteredValue
            state.previousOutput = output
            state.previousTimestamp = timestamp

            filteredValue = output
        }

        return filteredValue
    }

    private fun applyLowPassFilter(
        value: Double,
        timestamp: Long
    ): Double {
        val filterPasses = maxOf(1, PpgConfig.LOW_PASS_FILTER_PASSES)

        while (lowPassFilterStates.size < filterPasses) {
            lowPassFilterStates.add(
                LowPassFilterState()
            )
        }

        var filteredValue = value

        lowPassFilterStates.forEach { state ->
            val previousOutput = state.previousOutput
            val previousTimestamp = state.previousTimestamp

            if (previousOutput == null || previousTimestamp == null) {
                state.previousOutput = filteredValue
                state.previousTimestamp = timestamp

                return@forEach
            }

            val dt = maxOf(
                (timestamp - previousTimestamp) / 1_000.0,
                0.000001
            )

            val rc = 1.0 / (2.0 * Math.PI * PpgConfig.LOW_PASS_CUTOFF_HZ)
            val alpha = dt / (rc + dt)

            val output = previousOutput +
                    alpha * (filteredValue - previousOutput)

            state.previousOutput = output
            state.previousTimestamp = timestamp

            filteredValue = output
        }

        return filteredValue
    }
}