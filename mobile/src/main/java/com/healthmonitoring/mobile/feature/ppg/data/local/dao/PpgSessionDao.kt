package com.healthmonitoring.mobile.feature.ppg.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionEntity
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionSampleEntity

@Dao
interface PpgSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PpgSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSamples(samples: List<PpgSessionSampleEntity>)

    @Query("SELECT COUNT(*) FROM ppg_sessions")
    suspend fun getSessionCount(): Int

    @Query(
        """
        SELECT id
        FROM ppg_sessions
        ORDER BY startedAt ASC
        LIMIT 1
        """
    )
    suspend fun getOldestSessionId(): String?

    @Query("DELETE FROM ppg_sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Query(
        """
        SELECT *
        FROM ppg_sessions
        ORDER BY startedAt DESC
        """
    )
    suspend fun getSessions(): List<PpgSessionEntity>

    @Query(
        """
        SELECT *
        FROM ppg_session_samples
        WHERE sessionId = :sessionId
        ORDER BY sampleIndex ASC
        """
    )
    suspend fun getSamples(sessionId: String): List<PpgSessionSampleEntity>
}