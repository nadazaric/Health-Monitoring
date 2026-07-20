package com.healthmonitoring.mobile.feature.spo2.domain.use_case

import com.healthmonitoring.mobile.feature.spo2.domain.model.SpO2Measurement
import com.healthmonitoring.mobile.feature.spo2.domain.repository.SpO2Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSpO2UseCase @Inject constructor(
    private val spO2Repository: SpO2Repository
) {
    operator fun invoke(): Flow<SpO2Measurement> {
        return spO2Repository.observeSpO2()
    }
}