package com.healthmonitoring.mobile.feature.ppg.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionEntity
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionSampleEntity

data class PpgSessionWithSamples(
    @Embedded
    val session: PpgSessionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val samples: List<PpgSessionSampleEntity>
)