package com.healthmonitoring.mobile.feature.ppg.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitoring.mobile.feature.ppg.domain.use_case.ObservePpgSessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PpgViewModel @Inject constructor(
    private val observePpgSessionsUseCase: ObservePpgSessionsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(PpgState())
    val state: State<PpgState> = _state

    init {
        observeSessions()
    }

    fun selectSession(index: Int) {
        if (index !in _state.value.sessions.indices) {
            return
        }

        _state.value = _state.value.copy(
            selectedSessionIndex = index
        )
    }

    private fun observeSessions() {
        viewModelScope.launch {
            observePpgSessionsUseCase().collect { sessions ->
                _state.value = PpgState(
                    sessions = sessions,
                    selectedSessionIndex = 0
                )
            }
        }
    }
}