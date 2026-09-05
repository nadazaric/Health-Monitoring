package com.healthmonitoring.mobile.feature.ppg.domain.repository

import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgMeasurementSession

interface PpgSessionRepository {

    suspend fun saveSession(session: PpgMeasurementSession)
}