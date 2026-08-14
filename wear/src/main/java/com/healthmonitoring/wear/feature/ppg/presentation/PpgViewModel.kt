package com.healthmonitoring.wear.feature.ppg.presentation

import android.os.SystemClock
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.data.export.PpgCsvExporter
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.use_case.PpgUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PpgViewModel @Inject constructor(
    private val ppgUseCases: PpgUseCases,
    private val ppgCsvExporter: PpgCsvExporter
) : ViewModel() {

    private val _state = mutableStateOf(PpgState())
    val state: State<PpgState> = _state

    private val rawMeasurements =
        mutableListOf<PpgMeasurement>()

    private var measurementTimerJob: Job? = null

    init {
        observePpg()
        observeMeasurementErrors()
    }

    fun startMeasurement() {
        cancelMeasurementTimer()

        rawMeasurements.clear()

        _state.value = PpgState(
            remainingTimeMillis =
                PpgConfig.MEASUREMENT_DURATION_MILLIS,
            isMeasuring = true
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
                if (!_state.value.isMeasuring) {
                    return@collect
                }

                rawMeasurements.add(measurement)

                _state.value = _state.value.copy(
                    measurement = measurement,
                    errorMessage = null
                )
            }
        }
    }

    private fun startMeasurementTimer() {
        measurementTimerJob = viewModelScope.launch {
            val measurementStartTime =
                SystemClock.elapsedRealtime()

            while (
                isActive &&
                _state.value.isMeasuring
            ) {
                val elapsedTime =
                    SystemClock.elapsedRealtime() -
                            measurementStartTime

                val remainingTime =
                    (
                            PpgConfig.MEASUREMENT_DURATION_MILLIS -
                                    elapsedTime
                            ).coerceAtLeast(0L)

                _state.value = _state.value.copy(
                    remainingTimeMillis = remainingTime
                )

                if (remainingTime == 0L) {
                    finishMeasurement(
                        completed = true
                    )
                    break
                }

                delay(
                    PpgConfig.COUNTDOWN_INTERVAL_MILLIS
                )
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
                    rawMeasurements.clear()

                    _state.value = _state.value.copy(
                        isMeasuring = false,
                        errorMessage = message
                    )
                }
        }
    }

    private fun finishMeasurement(
        completed: Boolean
    ) {
        if (!_state.value.isMeasuring) {
            return
        }

        ppgUseCases.stopPpgMeasurement()
        cancelMeasurementTimer()

        val measurementsToExport =
            rawMeasurements.toList()

        _state.value = _state.value.copy(
            remainingTimeMillis = if (completed) {
                0L
            } else {
                _state.value.remainingTimeMillis
            },
            isMeasuring = false,
            isMeasurementCompleted = completed
        )

        exportRawMeasurements(
            measurements = measurementsToExport
        )
    }

    private fun exportRawMeasurements(
        measurements: List<PpgMeasurement>
    ) {
        if (measurements.isEmpty()) {
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

        super.onCleared()
    }
}