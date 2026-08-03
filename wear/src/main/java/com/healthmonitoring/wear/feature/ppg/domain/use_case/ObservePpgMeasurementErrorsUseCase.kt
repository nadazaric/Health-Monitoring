package com.healthmonitoring.wear.feature.ppg.domain.use_case

import com.healthmonitoring.wear.feature.ppg.domain.repository.PpgRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePpgMeasurementErrorsUseCase @Inject constructor(
    private val ppgRepository: PpgRepository
) {
    operator fun invoke(): Flow<String> =
        ppgRepository.observeMeasurementErrors()
}