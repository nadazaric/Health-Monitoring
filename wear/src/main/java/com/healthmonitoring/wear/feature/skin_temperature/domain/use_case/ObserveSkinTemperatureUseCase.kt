package com.healthmonitoring.wear.feature.skin_temperature.domain.use_case

import com.healthmonitoring.wear.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.healthmonitoring.wear.feature.skin_temperature.domain.repository.SkinTemperatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSkinTemperatureUseCase @Inject constructor(
    private val skinTemperatureRepository: SkinTemperatureRepository
) {
    operator fun invoke(): Flow<SkinTemperatureMeasurement> {
        return skinTemperatureRepository.observeSkinTemperature()
    }
}