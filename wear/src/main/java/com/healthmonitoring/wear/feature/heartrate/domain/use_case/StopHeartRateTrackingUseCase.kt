package com.healthmonitoring.wear.feature.heartrate.domain.use_case

import com.healthmonitoring.wear.feature.heartrate.domain.repository.HeartRateRepository
import javax.inject.Inject

class StopHeartRateTrackingUseCase @Inject constructor(
    private val heartRateRepository: HeartRateRepository
) {

    operator fun invoke() {
        heartRateRepository.stopTracking()
    }
}