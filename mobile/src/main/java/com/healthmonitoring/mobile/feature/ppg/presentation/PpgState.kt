package com.healthmonitoring.mobile.feature.ppg.presentation

import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgMeasurementSession

data class PpgState(
    val sessions: List<PpgMeasurementSession> = emptyList(),
    val selectedSessionIndex: Int = 0
) {

    val selectedSession: PpgMeasurementSession?
        get() = sessions.getOrNull(selectedSessionIndex)

    val hasMultipleSessions: Boolean
        get() = sessions.size > 1
}