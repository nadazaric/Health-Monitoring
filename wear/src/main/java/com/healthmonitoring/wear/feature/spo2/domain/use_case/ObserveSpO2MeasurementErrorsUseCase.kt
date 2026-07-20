package com.healthmonitoring.wear.feature.spo2.domain.use_case

import com.healthmonitoring.wear.feature.spo2.domain.repository.SpO2Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSpO2MeasurementErrorsUseCase @Inject constructor(
    private val spO2Repository: SpO2Repository
) {
    operator fun invoke(): Flow<String> {
        return spO2Repository.observeMeasurementErrors()
    }
}