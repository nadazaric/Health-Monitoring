package com.healthmonitoring.wear.feature.spo2.domain.use_case

import com.healthmonitoring.wear.feature.spo2.domain.repository.SpO2Repository
import javax.inject.Inject

class StopSpO2MeasurementUseCase @Inject constructor(
    private val spO2Repository: SpO2Repository
) {
    operator fun invoke() {
        spO2Repository.stopMeasurement()
    }
}