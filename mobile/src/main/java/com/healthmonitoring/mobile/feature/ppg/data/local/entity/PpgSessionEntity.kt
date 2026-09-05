package com.healthmonitoring.mobile.feature.ppg.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ppg_sessions")
data class PpgSessionEntity(
    @PrimaryKey
    val id: String,
    val startedAt: Long,
    val endedAt: Long
)