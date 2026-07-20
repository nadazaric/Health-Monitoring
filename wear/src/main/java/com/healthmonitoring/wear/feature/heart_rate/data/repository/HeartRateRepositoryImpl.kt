package com.healthmonitoring.wear.feature.heart_rate.data.repository

import android.util.Log
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.core.datalayer.HealthDataMessageSender
import com.healthmonitoring.wear.feature.heart_rate.data.listener.HeartRateListener
import com.healthmonitoring.wear.feature.heart_rate.domain.model.HeartRateMeasurement
import com.healthmonitoring.wear.feature.heart_rate.domain.repository.HeartRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeartRateRepositoryImpl @Inject constructor(
    private val heartRateListener: HeartRateListener,
    private val healthDataMessageSender: HealthDataMessageSender
) : HeartRateRepository {

    private val heartRateMeasurements = MutableSharedFlow<HeartRateMeasurement>(
        replay = 1
    )

    init {
        heartRateListener.setOnHeartRateChangedCallback { measurement ->
            val emitted = heartRateMeasurements.tryEmit(measurement)

            Log.d(
                Tags.HEART_RATE_REPOSITORY,
                "Heart rate measurement emitted. BPM: ${measurement.bpm}, status: ${measurement.status}, emitted: $emitted"
            )

            healthDataMessageSender.sendHeartRateMeasurement(measurement)
        }
    }

    override fun observeHeartRate(): Flow<HeartRateMeasurement> {
        return heartRateMeasurements.asSharedFlow()
    }

    override fun startTracking() {
        Log.d(Tags.HEART_RATE_REPOSITORY, "Starting heart rate tracking from repository.")

        heartRateListener.startTracking()
    }

    override fun stopTracking() {
        Log.d(Tags.HEART_RATE_REPOSITORY, "Stopping heart rate tracking from repository.")

        heartRateListener.stopTracking()
    }

}