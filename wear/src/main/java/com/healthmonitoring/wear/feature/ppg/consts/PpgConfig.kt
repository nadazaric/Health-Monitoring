package com.healthmonitoring.wear.feature.ppg.consts

import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgChannelSubtraction
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgDcRemovalType

object PpgConfig {

    const val CHART_MIN_VALUE_RANGE = 1.0
    const val CHART_WINDOW_MILLIS = 3_000L
    const val CSV_EXPORT_ENABLED = false

    const val STARTUP_TRIM_MILLIS = 600L
    const val PROCESSING_WARMUP_MILLIS = 2_000L
    const val MEASUREMENT_DURATION_MILLIS = 10_000L
    const val COUNTDOWN_INTERVAL_MILLIS = 1_000L

    // Algorithm parameters
    const val INVERT_PROCESSED_SIGNAL = true
    val CHANNEL_SUBTRACTION = PpgChannelSubtraction.RED_INFRARED_MEAN
    val DC_REMOVAL_TYPE = PpgDcRemovalType.CENTERED
    const val DC_REMOVAL_WINDOW_MILLIS = 2_000L

}