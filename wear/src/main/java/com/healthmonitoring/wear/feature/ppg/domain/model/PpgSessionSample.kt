package com.healthmonitoring.wear.feature.ppg.domain.model

import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgBreathingPhase

data class PpgSessionSample(
    val timestamp: Long,
    val green: Int,
    val red: Int,
    val infrared: Int,
    val processedValue: Double,
    val breathingPhase: PpgBreathingPhase
)