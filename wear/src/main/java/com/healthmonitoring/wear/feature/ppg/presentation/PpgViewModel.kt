package com.healthmonitoring.wear.feature.ppg.presentation

import android.os.SystemClock
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.data.export.PpgCsvExporter
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgRawSample
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgPeak
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedSample
import com.healthmonitoring.wear.feature.ppg.domain.use_case.PpgUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import com.healthmonitoring.wear.feature.ppg.domain.processing.PpgSignalProcessor

@HiltViewModel
class PpgViewModel @Inject constructor(
    private val ppgUseCases: PpgUseCases,
    private val ppgSignalProcessor: PpgSignalProcessor,
    private val ppgCsvExporter: PpgCsvExporter
) : ViewModel() {

    private val _state = mutableStateOf(PpgState())
    val state: State<PpgState> = _state

    private val rawMeasurements = mutableListOf<PpgRawSample>()
    private val chartMeasurements = ArrayDeque<PpgProcessedSample>()
    private val chartPeaks = ArrayDeque<PpgPeak>()

    private var measurementTimerJob: Job? = null

    private var breathingJob: Job? = null

    init {
        observePpg()
        observeMeasurementErrors()
    }

    fun startMeasurement() {
        cancelMeasurementTimer()
        cancelBreathingGuidance()

        rawMeasurements.clear()
        chartMeasurements.clear()
        chartPeaks.clear()
        ppgSignalProcessor.reset()

        _state.value = PpgState(
            remainingTimeMillis = PpgConfig.MEASUREMENT_DURATION_MILLIS,
            measurementPhase = PpgMeasurementPhase.STARTUP_TRIM
        )

        ppgUseCases.startPpgMeasurement()
        startMeasurementTimer()
    }

    fun stopMeasurement() {
        finishMeasurement(
            completed = false
        )
    }

    private fun observePpg() {
        viewModelScope.launch {
            ppgUseCases.observePpg().collect { measurement ->
                when (_state.value.measurementPhase) {
                    PpgMeasurementPhase.STARTUP_TRIM -> {
                        return@collect
                    }

                    PpgMeasurementPhase.PROCESSING_WARMUP -> {
                        ppgSignalProcessor.process(
                            measurement = measurement
                        )
                    }

                    PpgMeasurementPhase.MEASURING -> {
                        val processingResults = ppgSignalProcessor.process(
                            measurement = measurement
                        )

                        rawMeasurements.add(measurement)

                        processingResults.forEach { result ->
                            result.peak?.let { peak ->
                                addChartPeak(peak)
                            }

                            addChartMeasurement(
                                measurement = result.measurement
                            )

                            result.heartRate?.let { heartRate ->
                                _state.value = _state.value.copy(
                                    heartRate = heartRate
                                )
                            }
                        }
                    }

                    PpgMeasurementPhase.IDLE, PpgMeasurementPhase.COMPLETED -> {
                        return@collect
                    }
                }
            }
        }
    }

    private fun startMeasurementTimer() {
        measurementTimerJob = viewModelScope.launch {
            delay(PpgConfig.STARTUP_TRIM_MILLIS.milliseconds)

            if (!isActive || !_state.value.isMeasurementActive) {
                return@launch
            }

            _state.value = _state.value.copy(
                measurementPhase = PpgMeasurementPhase.PROCESSING_WARMUP
            )

            delay(PpgConfig.PROCESSING_WARMUP_MILLIS.milliseconds)

            if (!isActive || !_state.value.isMeasurementActive) {
                return@launch
            }

            ppgSignalProcessor.resetMeasurementState()

            _state.value = _state.value.copy(
                measurementPhase = PpgMeasurementPhase.MEASURING
            )

            startBreathingGuidance()

            val measurementStartTime = SystemClock.elapsedRealtime()

            while (isActive && _state.value.isMeasuring) {
                val elapsedTime = SystemClock.elapsedRealtime() - measurementStartTime

                val remainingTime =
                    (PpgConfig.MEASUREMENT_DURATION_MILLIS - elapsedTime)
                        .coerceAtLeast(0L)

                _state.value = _state.value.copy(
                    remainingTimeMillis = remainingTime
                )

                if (remainingTime == 0L) {
                    finishMeasurement(
                        completed = true
                    )
                    break
                }

                delay(PpgConfig.COUNTDOWN_INTERVAL_MILLIS.milliseconds)
            }
        }
    }

    private fun observeMeasurementErrors() {
        viewModelScope.launch {
            ppgUseCases
                .observeMeasurementErrors()
                .collect { message ->
                    ppgUseCases.stopPpgMeasurement()
                    cancelMeasurementTimer()
                    cancelBreathingGuidance()
                    rawMeasurements.clear()

                    _state.value = _state.value.copy(
                        measurementPhase = PpgMeasurementPhase.IDLE,
                        breathingPhase = null,
                        errorMessage = message
                    )
                }
        }
    }

    private fun finishMeasurement(completed: Boolean) {
        if (!_state.value.isMeasurementActive) {
            return
        }

        ppgUseCases.stopPpgMeasurement()
        cancelMeasurementTimer()

        val measurementsToExport = rawMeasurements.toList()

        _state.value = _state.value.copy(
            remainingTimeMillis = if (completed) {
                0L
            } else {
                _state.value.remainingTimeMillis
            },
            measurementPhase = if (completed) {
                PpgMeasurementPhase.COMPLETED
            } else {
                PpgMeasurementPhase.IDLE
            },
            breathingPhase = null
        )

        exportRawMeasurements(
            measurements = measurementsToExport
        )

        cancelBreathingGuidance()
    }

    private fun exportRawMeasurements(measurements: List<PpgRawSample>) {
        if (!PpgConfig.CSV_EXPORT_ENABLED || measurements.isEmpty()) {
            return
        }

        viewModelScope.launch {
            ppgCsvExporter.export(
                measurements = measurements
            )
        }
    }

    private fun cancelMeasurementTimer() {
        measurementTimerJob?.cancel()
        measurementTimerJob = null
    }

    override fun onCleared() {
        ppgUseCases.stopPpgMeasurement()
        cancelMeasurementTimer()
        cancelBreathingGuidance()

        super.onCleared()
    }

    private fun addChartMeasurement(measurement: PpgProcessedSample) {
        chartMeasurements.addLast(measurement)

        val minimumTimestamp = measurement.timestamp - PpgConfig.CHART_WINDOW_MILLIS

        while (chartMeasurements.isNotEmpty() && chartMeasurements.first().timestamp < minimumTimestamp) {
            chartMeasurements.removeFirst()
        }

        while (chartPeaks.isNotEmpty() && chartPeaks.first().timestamp < minimumTimestamp) {
            chartPeaks.removeFirst()
        }

        _state.value = _state.value.copy(
            chartMeasurements = chartMeasurements.toList(),
            chartPeaks = chartPeaks.toList(),
            errorMessage = null
        )
    }

    private fun addChartPeak(peak: PpgPeak) {
        val lastPeak = chartPeaks.lastOrNull()

        if (lastPeak != null && peak.timestamp - lastPeak.timestamp < PpgConfig.PEAK_MIN_DISTANCE_MILLIS) {
            chartPeaks.removeLast()
        }

        chartPeaks.addLast(peak)
    }

    private fun getBreathingPhaseDuration(phase: PpgBreathingPhase): Long {
        return when (phase) {
            PpgBreathingPhase.INHALE ->
                PpgConfig.BREATHING_INHALE_DURATION_MILLIS

            PpgBreathingPhase.INHALE_HOLD ->
                PpgConfig.BREATHING_INHALE_HOLD_DURATION_MILLIS

            PpgBreathingPhase.EXHALE ->
                PpgConfig.BREATHING_EXHALE_DURATION_MILLIS

            PpgBreathingPhase.EXHALE_HOLD ->
                PpgConfig.BREATHING_EXHALE_HOLD_DURATION_MILLIS
        }
    }

    private fun startBreathingGuidance() {
        breathingJob?.cancel()

        breathingJob = viewModelScope.launch {
            val phases = PpgBreathingPhase.entries

            while (isActive && _state.value.isMeasuring) {
                phases.forEach { phase ->
                    if (!isActive || !_state.value.isMeasuring) {
                        return@launch
                    }

                    val durationMillis = getBreathingPhaseDuration(phase)

                    if (durationMillis <= 0L) {
                        return@forEach
                    }

                    _state.value = _state.value.copy(breathingPhase = phase)
                    delay(durationMillis.milliseconds)
                }
            }
        }
    }

    private fun cancelBreathingGuidance() {
        breathingJob?.cancel()
        breathingJob = null
    }
}