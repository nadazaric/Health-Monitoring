package com.healthmonitoring.mobile.feature.skin_temperature.domain.use_case

import javax.inject.Inject

data class SkinTemperatureUseCases @Inject constructor(
    val observeSkinTemperature: ObserveSkinTemperatureUseCase
)