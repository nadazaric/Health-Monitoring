package com.healthmonitoring.wear.feature.heart_rate.domain.use_case

import javax.inject.Inject

data class HeartRateUseCases @Inject constructor(
    val observeHeartRate: ObserveHeartRateUseCase,
    val startHeartRateTracking: StartHeartRateTrackingUseCase,
    val stopHeartRateTracking: StopHeartRateTrackingUseCase
)