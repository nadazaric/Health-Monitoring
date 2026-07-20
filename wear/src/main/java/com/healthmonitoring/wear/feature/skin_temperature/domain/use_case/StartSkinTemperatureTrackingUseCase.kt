package com.healthmonitoring.wear.feature.skin_temperature.domain.use_case

import com.healthmonitoring.wear.feature.skin_temperature.domain.repository.SkinTemperatureRepository
import javax.inject.Inject

class StartSkinTemperatureTrackingUseCase @Inject constructor(
    private val skinTemperatureRepository: SkinTemperatureRepository
) {
    operator fun invoke() {
        skinTemperatureRepository.startTracking()
    }
}