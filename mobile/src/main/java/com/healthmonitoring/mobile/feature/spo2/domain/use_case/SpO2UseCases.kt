package com.healthmonitoring.mobile.feature.spo2.domain.use_case

import javax.inject.Inject

data class SpO2UseCases @Inject constructor(
    val observeSpO2: ObserveSpO2UseCase,
    val observeSpO2MeasurementState: ObserveSpO2MeasurementStateUseCase
)