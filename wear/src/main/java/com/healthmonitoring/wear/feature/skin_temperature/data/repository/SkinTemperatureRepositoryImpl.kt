package com.healthmonitoring.wear.feature.skin_temperature.data.repository

import android.util.Log
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.core.datalayer.HealthDataMessageSender
import com.healthmonitoring.wear.feature.skin_temperature.data.listener.SkinTemperatureListener
import com.healthmonitoring.wear.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.healthmonitoring.wear.feature.skin_temperature.domain.repository.SkinTemperatureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkinTemperatureRepositoryImpl @Inject constructor(
    private val skinTemperatureListener: SkinTemperatureListener,
    private val healthDataMessageSender: HealthDataMessageSender
) : SkinTemperatureRepository {

    private val skinTemperatureMeasurements =
        MutableSharedFlow<SkinTemperatureMeasurement>(replay = 1)

    init {
        skinTemperatureListener.setOnSkinTemperatureChangedCallback { measurement ->
            skinTemperatureMeasurements.tryEmit(measurement)
            healthDataMessageSender.sendSkinTemperatureMeasurement(measurement)
        }
    }

    override fun observeSkinTemperature(): Flow<SkinTemperatureMeasurement> {
        return skinTemperatureMeasurements.asSharedFlow()
    }

    override fun startTracking() {
        Log.d(
            Tags.SKIN_TEMPERATURE_REPOSITORY,
            "Starting skin temperature tracking."
        )

        skinTemperatureListener.startTracking()
    }

    override fun stopTracking() {
        Log.d(
            Tags.SKIN_TEMPERATURE_REPOSITORY,
            "Stopping skin temperature tracking."
        )

        skinTemperatureListener.stopTracking()
    }
}