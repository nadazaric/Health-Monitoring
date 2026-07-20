package com.healthmonitoring.mobile.feature.spo2.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}