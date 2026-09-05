package com.healthmonitoring.mobile.feature.ppg.domain.model

import com.healthmonitoring.mobile.feature.ppg.domain.enumeration.PpgBreathingPhase

data class PpgSessionSample(
    val timestamp: Long,
    val green: Int,
    val red: Int,
    val infrared: Int,
    val processedValue: Double,
    val breathingPhase: PpgBreathingPhase
)