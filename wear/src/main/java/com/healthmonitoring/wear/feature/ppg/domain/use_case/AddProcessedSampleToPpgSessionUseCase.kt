package com.healthmonitoring.wear.feature.ppg.domain.use_case

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedSample
import com.healthmonitoring.wear.feature.ppg.domain.repository.PpgRepository
import javax.inject.Inject

class AddProcessedSampleToPpgSessionUseCase @Inject constructor(
    private val ppgRepository: PpgRepository
) {
    operator fun invoke(sample: PpgProcessedSample) {
        ppgRepository.addProcessedSampleToSession(sample)
    }
}