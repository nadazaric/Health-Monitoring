package com.healthmonitoring.wear.feature.ppg.domain.use_case

import com.healthmonitoring.wear.feature.ppg.domain.repository.PpgRepository
import javax.inject.Inject

class StartPpgSessionUseCase @Inject constructor(
    private val ppgRepository: PpgRepository
) {
    operator fun invoke(startedAt: Long) {
        ppgRepository.startSession(startedAt)
    }
}