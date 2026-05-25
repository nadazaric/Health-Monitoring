package com.master.healthmonitoring.feature.heartrate.domain.use_case

import com.master.healthmonitoring.feature.heartrate.domain.repository.HeartRateRepository
import javax.inject.Inject

class StopHeartRateTrackingUseCase @Inject constructor(
    private val heartRateRepository: HeartRateRepository
) {

    operator fun invoke() {
        heartRateRepository.stopTracking()
    }
}