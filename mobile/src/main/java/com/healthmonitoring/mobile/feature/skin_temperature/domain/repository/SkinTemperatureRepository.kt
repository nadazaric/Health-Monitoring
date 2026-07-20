package com.healthmonitoring.mobile.feature.skin_temperature.domain.repository

import com.healthmonitoring.mobile.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import kotlinx.coroutines.flow.Flow

interface SkinTemperatureRepository {
    fun observeSkinTemperature(): Flow<SkinTemperatureMeasurement>
}