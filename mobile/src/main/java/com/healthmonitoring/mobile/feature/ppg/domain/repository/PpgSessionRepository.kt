package com.healthmonitoring.mobile.feature.ppg.domain.repository

import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgMeasurementSession
import kotlinx.coroutines.flow.Flow

interface PpgSessionRepository {

    suspend fun saveSession(session: PpgMeasurementSession)

    fun observeSessions(): Flow<List<PpgMeasurementSession>>
}