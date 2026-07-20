package com.healthmonitoring.wear.feature.spo2.domain.use_case

import javax.inject.Inject

data class SpO2UseCases @Inject constructor(
    val observeSpO2: ObserveSpO2UseCase,
    val observeMeasurementErrors: ObserveSpO2MeasurementErrorsUseCase,
    val startSpO2Measurement: StartSpO2MeasurementUseCase,
    val stopSpO2Measurement: StopSpO2MeasurementUseCase
)