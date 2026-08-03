package com.healthmonitoring.wear.feature.ppg.domain.use_case

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.healthmonitoring.wear.feature.ppg.domain.repository.PpgRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePpgUseCase @Inject constructor(
    private val ppgRepository: PpgRepository
) {
    operator fun invoke(): Flow<PpgMeasurement> =
        ppgRepository.observePpg()
}