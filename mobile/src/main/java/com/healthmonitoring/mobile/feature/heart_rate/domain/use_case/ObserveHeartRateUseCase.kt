package com.healthmonitoring.mobile.feature.heart_rate.domain.use_case

import com.healthmonitoring.mobile.feature.heart_rate.domain.model.HeartRateMeasurement
import com.healthmonitoring.mobile.feature.heart_rate.domain.repository.HeartRateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHeartRateUseCase @Inject constructor(
    private val heartRateRepository: HeartRateRepository
) {
    operator fun invoke(): Flow<HeartRateMeasurement> {
        return heartRateRepository.observeHeartRate()
    }
}