package com.healthmonitoring.mobile.feature.ppg.data.repository

import androidx.room.withTransaction
import com.healthmonitoring.mobile.core.database.HealthMonitoringDatabase
import com.healthmonitoring.mobile.feature.ppg.consts.PpgConfig
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionEntity
import com.healthmonitoring.mobile.feature.ppg.data.local.entity.PpgSessionSampleEntity
import com.healthmonitoring.mobile.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgMeasurementSession
import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgSessionSample
import com.healthmonitoring.mobile.feature.ppg.domain.repository.PpgSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PpgSessionRepositoryImpl @Inject constructor(
    private val database: HealthMonitoringDatabase
) : PpgSessionRepository {

    private val ppgSessionDao = database.ppgSessionDao()

    override suspend fun saveSession(session: PpgMeasurementSession) {
        database.withTransaction {
            ppgSessionDao.deleteSession(session.id)

            if (ppgSessionDao.getSessionCount() >= PpgConfig.MAX_STORED_SESSIONS) {
                ppgSessionDao.getOldestSessionId()?.let { sessionId ->
                    ppgSessionDao.deleteSession(sessionId)
                }
            }

            ppgSessionDao.insertSession(
                PpgSessionEntity(
                    id = session.id,
                    startedAt = session.startedAt,
                    endedAt = session.endedAt
                )
            )

            ppgSessionDao.insertSamples(
                session.samples.mapIndexed { index, sample ->
                    PpgSessionSampleEntity(
                        sessionId = session.id,
                        sampleIndex = index,
                        timestamp = sample.timestamp,
                        green = sample.green,
                        red = sample.red,
                        infrared = sample.infrared,
                        processedValue = sample.processedValue,
                        breathingPhase = sample.breathingPhase.name
                    )
                }
            )
        }
    }

    override fun observeSessions(): Flow<List<PpgMeasurementSession>> {
        return ppgSessionDao.observeSessions().map { sessions ->
            sessions.map { sessionWithSamples ->
                PpgMeasurementSession(
                    id = sessionWithSamples.session.id,
                    startedAt = sessionWithSamples.session.startedAt,
                    endedAt = sessionWithSamples.session.endedAt,
                    samples = sessionWithSamples.samples
                        .sortedBy { it.sampleIndex }
                        .map { sample ->
                            PpgSessionSample(
                                timestamp = sample.timestamp,
                                green = sample.green,
                                red = sample.red,
                                infrared = sample.infrared,
                                processedValue = sample.processedValue,
                                breathingPhase = PpgBreathingPhase.valueOf(sample.breathingPhase)
                            )
                        }
                )
            }
        }
    }
}