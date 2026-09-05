package com.healthmonitoring.mobile.feature.ppg.domain.use_case

import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgMeasurementSession
import com.healthmonitoring.mobile.feature.ppg.domain.repository.PpgSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePpgSessionsUseCase @Inject constructor(
    private val ppgSessionRepository: PpgSessionRepository
) {

    operator fun invoke(): Flow<List<PpgMeasurementSession>> {
        return ppgSessionRepository.observeSessions()
    }
}