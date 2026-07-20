package com.healthmonitoring.wear.feature.skin_temperature.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.wear.feature.skin_temperature.domain.use_case.SkinTemperatureUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkinTemperatureViewModel @Inject constructor(
    private val skinTemperatureUseCases: SkinTemperatureUseCases
) : ViewModel() {

    private val _state = mutableStateOf(SkinTemperatureState())
    val state: State<SkinTemperatureState> = _state

    init {
        observeSkinTemperature()
    }

    private fun observeSkinTemperature() {
        viewModelScope.launch {
            skinTemperatureUseCases.observeSkinTemperature().collect { measurement ->
                _state.value = _state.value.copy(
                    objectTemperature = measurement.objectTemperature,
                    ambientTemperature = measurement.ambientTemperature,
                    status = measurement.status,
                    timestamp = measurement.timestamp
                )
            }
        }
    }
}