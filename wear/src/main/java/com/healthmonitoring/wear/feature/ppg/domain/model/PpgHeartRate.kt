package com.healthmonitoring.wear.feature.ppg.domain.model

data class PpgHeartRate(
    val currentBpm: Double?,
    val averageBpm: Double?,
    val peakCount: Int
)