package com.healthmonitoring.mobile.feature.heartrate.domain.use_case

import com.healthmonitoring.mobile.feature.heartrate.domain.model.HeartRateMeasurement
import com.healthmonitoring.mobile.feature.heartrate.domain.repository.HeartRateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHeartRateUseCase @Inject constructor(
    private val heartRateRepository: HeartRateRepository
) {
    operator fun invoke(): Flow<HeartRateMeasurement> {
        return heartRateRepository.observeHeartRate()
    }
}