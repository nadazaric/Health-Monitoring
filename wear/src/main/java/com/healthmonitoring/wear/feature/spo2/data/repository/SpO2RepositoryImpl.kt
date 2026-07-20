package com.healthmonitoring.wear.feature.spo2.data.repository

import android.util.Log
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.core.datalayer.HealthDataMessageSender
import com.healthmonitoring.wear.feature.spo2.data.listener.SpO2Listener
import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2Measurement
import com.healthmonitoring.wear.feature.spo2.domain.repository.SpO2Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpO2RepositoryImpl @Inject constructor(
    private val spO2Listener: SpO2Listener,
    private val healthDataMessageSender: HealthDataMessageSender
) : SpO2Repository {

    private val spO2Measurements =
        MutableSharedFlow<SpO2Measurement>(replay = 1)

    private val measurementErrors =
        MutableSharedFlow<String>(
            replay = 0,
            extraBufferCapacity = 1
        )

    init {
        spO2Listener.setOnSpO2MeasuredCallback { measurement ->
            spO2Measurements.tryEmit(measurement)
            healthDataMessageSender.sendSpO2Measurement(measurement)
        }

        spO2Listener.setOnMeasurementFailedCallback { message ->
            measurementErrors.tryEmit(message)
        }
    }

    override fun observeSpO2(): Flow<SpO2Measurement> {
        return spO2Measurements.asSharedFlow()
    }

    override fun observeMeasurementErrors(): Flow<String> {
        return measurementErrors.asSharedFlow()
    }

    override fun startMeasurement() {
        Log.d(
            Tags.SPO2_REPOSITORY,
            "Starting SpO2 measurement."
        )

        spO2Listener.startMeasurement()
    }

    override fun stopMeasurement() {
        Log.d(
            Tags.SPO2_REPOSITORY,
            "Stopping SpO2 measurement."
        )

        spO2Listener.stopMeasurement()
    }
}