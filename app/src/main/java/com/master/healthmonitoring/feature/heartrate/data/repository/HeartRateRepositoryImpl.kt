package com.master.healthmonitoring.feature.heartrate.data.repository

import android.util.Log
import com.master.healthmonitoring.consts.Tags
import com.master.healthmonitoring.feature.heartrate.data.listener.HeartRateListener
import com.master.healthmonitoring.feature.heartrate.domain.model.HeartRateMeasurement
import com.master.healthmonitoring.feature.heartrate.domain.repository.HeartRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeartRateRepositoryImpl @Inject constructor(
    private val heartRateListener: HeartRateListener
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