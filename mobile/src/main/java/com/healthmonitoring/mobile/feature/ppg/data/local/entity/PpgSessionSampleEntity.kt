package com.healthmonitoring.mobile.feature.ppg.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "ppg_session_samples",
    primaryKeys = ["sessionId", "sampleIndex"],
    foreignKeys = [
        ForeignKey(
            entity = PpgSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sessionId"])
    ]
)
data class PpgSessionSampleEntity(
    val sessionId: String,
    val sampleIndex: Int,
    val timestamp: Long,
    val green: Int,
    val red: Int,
    val infrared: Int,
    val processedValue: Double,
    val breathingPhase: String
)