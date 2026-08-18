package com.healthmonitoring.wear.feature.ppg.consts

import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgChannelSubtraction
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgDcRemovalType
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgFilterType

object PpgConfig {

    const val CHART_MIN_VALUE_RANGE = 1.0
    const val CHART_WINDOW_MILLIS = 2_000L
    const val CSV_EXPORT_ENABLED = false

    const val STARTUP_TRIM_MILLIS = 600L
    const val PROCESSING_WARMUP_MILLIS = 2_000L
    const val MEASUREMENT_DURATION_MILLIS = 10_000L
    const val COUNTDOWN_INTERVAL_MILLIS = 1_000L

    // Algorithm parameters
    const val INVERT_PROCESSED_SIGNAL = true
    val CHANNEL_SUBTRACTION = PpgChannelSubtraction.NONE

    val DC_REMOVAL_TYPE = PpgDcRemovalType.CENTERED
    const val DC_REMOVAL_WINDOW_MILLIS = 2_000L

    val FILTER_TYPE = PpgFilterType.BAND_PASS
    const val HIGH_PASS_CUTOFF_HZ = 0.7
    const val HIGH_PASS_FILTER_PASSES = 1
    const val LOW_PASS_CUTOFF_HZ = 5.0
    const val LOW_PASS_FILTER_PASSES = 1

    const val PEAK_DETECTION_ENABLED = true
    const val PEAK_MIN_DISTANCE_MILLIS = 400L
    const val PEAK_MIN_HEIGHT = 0.0

    const val HEART_RATE_ENABLED = true
    const val HEART_RATE_AVERAGING_INTERVAL_COUNT = 5
    const val HEART_RATE_MIN_BPM = 40.0
    const val HEART_RATE_MAX_BPM = 180.0

}