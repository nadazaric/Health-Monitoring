package com.healthmonitoring.wear.feature.ppg.domain.use_case

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurementSession
import com.healthmonitoring.wear.feature.ppg.domain.repository.PpgRepository
import javax.inject.Inject

class FinishPpgSessionUseCase @Inject constructor(
    private val ppgRepository: PpgRepository
) {
    operator fun invoke(endedAt: Long): PpgMeasurementSession {
        return ppgRepository.finishSession(endedAt)
    }
}