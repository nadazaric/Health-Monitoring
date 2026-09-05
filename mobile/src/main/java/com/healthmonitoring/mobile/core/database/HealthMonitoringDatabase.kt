package com.healthmonitoring.mobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.healthmonitoring.mobile.feature.ppg.data.local.dao.PpgSessionDao
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionEntity
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionSampleEntity

@Database(
    entities = [
        PpgSessionEntity::class,
        PpgSessionSampleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HealthMonitoringDatabase : RoomDatabase() {

    abstract fun ppgSessionDao(): PpgSessionDao
}