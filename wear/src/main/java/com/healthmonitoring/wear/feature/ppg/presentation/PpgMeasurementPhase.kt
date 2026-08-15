package com.healthmonitoring.wear.feature.ppg.presentation

enum class PpgMeasurementPhase {
    IDLE,
    STARTUP_TRIM,
    PROCESSING_WARMUP,
    MEASURING,
    COMPLETED
}