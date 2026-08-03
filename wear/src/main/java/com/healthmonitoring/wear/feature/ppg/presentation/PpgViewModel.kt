package com.healthmonitoring.wear.feature.ppg.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.wear.feature.ppg.domain.use_case.PpgUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PpgViewModel @Inject constructor(
    private val ppgUseCases: PpgUseCases
) : ViewModel() {

    private val _state = mutableStateOf(PpgState())
    val state: State<PpgState> = _state

    init {
        observePpg()
        observeMeasurementErrors()
    }

    fun startMeasurement() {
        _state.value = PpgState(
            isMeasuring = true
        )

        ppgUseCases.startPpgMeasurement()
    }

    fun stopMeasurement() {
        ppgUseCases.stopPpgMeasurement()

        _state.value = _state.value.copy(
            isMeasuring = false
        )
    }

    private fun observePpg() {
        viewModelScope.launch {
            ppgUseCases.observePpg().collect { measurement ->
                _state.value = _state.value.copy(
                    measurement = measurement,
                    errorMessage = null
                )
            }
        }
    }

    private fun observeMeasurementErrors() {
        viewModelScope.launch {
            ppgUseCases.observeMeasurementErrors().collect { message ->
                _state.value = _state.value.copy(
                    isMeasuring = false,
                    errorMessage = message
                )
            }
        }
    }

    override fun onCleared() {
        ppgUseCases.stopPpgMeasurement()
        super.onCleared()
    }
}