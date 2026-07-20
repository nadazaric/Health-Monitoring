package com.healthmonitoring.wear.feature.heart_rate.domain.use_case

import com.healthmonitoring.wear.feature.heart_rate.domain.repository.HeartRateRepository
import javax.inject.Inject

class StartHeartRateTrackingUseCase @Inject constructor(
    private val heartRateRepository: HeartRateRepository
) {

    operator fun invoke() {
        heartRateRepository.startTracking()
    }

}