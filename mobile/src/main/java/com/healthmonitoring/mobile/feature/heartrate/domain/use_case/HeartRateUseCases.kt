package com.healthmonitoring.mobile.feature.heartrate.domain.use_case

import javax.inject.Inject

data class HeartRateUseCases @Inject constructor(
    val observeHeartRate: ObserveHeartRateUseCase
)