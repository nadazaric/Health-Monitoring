package com.healthmonitoring.mobile.feature.skin_temperature.data.repository

import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.healthmonitoring.mobile.feature.skin_temperature.domain.repository.SkinTemperatureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkinTemperatureRepositoryImpl @Inject constructor(
    private val healthDataReceiver: HealthDataReceiver
) : SkinTemperatureRepository {

    override fun observeSkinTemperature(): Flow<SkinTemperatureMeasurement> {
        return healthDataReceiver.observeSkinTemperature()
    }
}