package com.healthmonitoring.mobile.feature.spo2.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.mobile.feature.spo2.domain.model.SpO2MeasurementState
import com.healthmonitoring.mobile.feature.spo2.domain.model.SpO2MeasurementStateUpdate
import com.healthmonitoring.mobile.feature.spo2.domain.use_case.SpO2UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpO2ViewModel @Inject constructor(
    private val spO2UseCases: SpO2UseCases
) : ViewModel() {

    private val _state = mutableStateOf(SpO2State())
    val state: State<SpO2State> = _state

    init {
        observeSpO2()
        observeSpO2MeasurementState()
    }

    private fun observeSpO2() {
        viewModelScope.launch {
            spO2UseCases.observeSpO2().collect { measurement ->
                _state.value = _state.value.copy(
                    spo2 = measurement.spo2,
                    heartRate = measurement.heartRate,
                    status = measurement.status,
                    timestamp = measurement.timestamp
                )
            }
        }
    }

    private fun observeSpO2MeasurementState() {
        viewModelScope.launch {
            spO2UseCases.observeSpO2MeasurementState().collect { stateUpdate ->
                val measurementState = resolveMeasurementState(stateUpdate)

                _state.value = _state.value.copy(
                    measurementState = measurementState,
                    errorMessage = stateUpdate.errorMessage
                )
            }
        }
    }

    private fun resolveMeasurementState(
        stateUpdate: SpO2MeasurementStateUpdate
    ): SpO2MeasurementState {
        val isStaleMeasuringState =
            stateUpdate.measurementState == SpO2MeasurementState.MEASURING &&
                    System.currentTimeMillis() - stateUpdate.updatedAt >
                    MEASUREMENT_STATE_TIMEOUT_MS

        return if (isStaleMeasuringState) {
            SpO2MeasurementState.FAILED
        } else {
            stateUpdate.measurementState
        }
    }

    companion object {
        private const val MEASUREMENT_STATE_TIMEOUT_MS = 90_000L
    }
}