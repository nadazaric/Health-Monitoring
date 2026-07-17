package com.healthmonitoring.mobile.feature.heartrate.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.mobile.feature.heartrate.domain.use_case.HeartRateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    private val heartRateUseCases: HeartRateUseCases
) : ViewModel() {

    private val _state = mutableStateOf(HeartRateState())
    val state: State<HeartRateState> = _state

    init {
        observeHeartRate()
    }

    private fun observeHeartRate() {
        viewModelScope.launch {
            heartRateUseCases.observeHeartRate().collect { measurement ->
                _state.value = _state.value.copy(
                    bpm = measurement.bpm,
                    status = measurement.status,
                    timestamp = measurement.timestamp
                )
            }
        }
    }
}