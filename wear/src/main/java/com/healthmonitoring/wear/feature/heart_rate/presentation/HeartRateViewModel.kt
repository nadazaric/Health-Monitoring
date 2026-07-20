package com.healthmonitoring.wear.feature.heart_rate.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.wear.feature.heart_rate.domain.use_case.HeartRateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    private val useCases: HeartRateUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HeartRateState())
    val state: State<HeartRateState> = _state

    init {
        observeHeartRate()
    }

    fun onEvent(event: HeartRateEvent) {
        when (event) {
            is HeartRateEvent.MonitoringStarted -> _state.value = _state.value.copy(
                isTracking = true,
                errorMessage = null
            )

            is HeartRateEvent.MonitoringStopped -> _state.value = _state.value.copy(
                isTracking = false
            )

            is HeartRateEvent.PermissionDenied -> _state.value = _state.value.copy(
                isTracking = false,
                errorMessage = "Heart rate permission denied."
            )
        }
    }

    private fun observeHeartRate() {
        viewModelScope.launch {
            useCases.observeHeartRate().collect { measurement ->
                _state.value = _state.value.copy(
                    bpm = measurement.bpm,
                    status = measurement.status,
                    timestamp = measurement.timestamp,
                    errorMessage = null
                )
            }
        }
    }
}