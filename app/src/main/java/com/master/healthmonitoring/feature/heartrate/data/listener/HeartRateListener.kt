package com.master.healthmonitoring.feature.heartrate.data.listener

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.master.healthmonitoring.consts.Tags
import com.master.healthmonitoring.core.HealthTrackerProvider
import com.master.healthmonitoring.feature.heartrate.domain.model.HeartRateMeasurement
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeartRateListener @Inject constructor(
    private val healthTrackerProvider: HealthTrackerProvider
) {
    private var heartRateTracker: HealthTracker? = null

    private val heartRateHandler = Handler(Looper.getMainLooper())

    private var onHeartRateChangedCallback: ((HeartRateMeasurement) -> Unit)? = null

    private val trackerEventListener = object : HealthTracker.TrackerEventListener {

        override fun onDataReceived(dataPoints: MutableList<DataPoint>) {
            dataPoints.forEach { dataPoint ->
                val status = dataPoint.getValue(
                    ValueKey.HeartRateSet.HEART_RATE_STATUS
                )

                val heartRate = dataPoint.getValue(
                    ValueKey.HeartRateSet.HEART_RATE
                )

                Log.i(
                    Tags.HEART_RATE_LISTENER,
                    "Heart rate received. BPM: $heartRate, status: $status, timestamp: ${dataPoint.timestamp}"
                )

                if (status == 1) {
                    val measurement = HeartRateMeasurement(
                        bpm = heartRate,
                        status = status,
                        timestamp = dataPoint.timestamp
                    )

                    onHeartRateChangedCallback?.invoke(measurement)
                }
            }
        }

        override fun onFlushCompleted() {
            Log.i(Tags.HEART_RATE_LISTENER, "Heart rate tracker flush completed.")
        }

        override fun onError(error: HealthTracker.TrackerError) {
            Log.e(Tags.HEART_RATE_LISTENER, "Heart rate tracker error: $error")
        }
    }

    fun startTracking() {
        if (heartRateTracker != null) {
            Log.i(Tags.HEART_RATE_LISTENER, "Heart rate tracking is already started.")
            return
        }

        val tracker = healthTrackerProvider.getTracker(
            HealthTrackerType.HEART_RATE_CONTINUOUS
        )

        if (tracker == null) {
            Log.w(Tags.HEART_RATE_LISTENER, "Heart rate tracker is not available.")
            return
        }

        heartRateTracker = tracker

        heartRateHandler.post {
            Log.i(Tags.HEART_RATE_LISTENER, "Starting heart rate tracking.")

            heartRateTracker?.setEventListener(trackerEventListener)
        }
    }

    fun stopTracking() {
        heartRateHandler.post {
            Log.i(Tags.HEART_RATE_LISTENER, "Stopping heart rate tracking.")

            heartRateTracker?.unsetEventListener()
            heartRateTracker = null
        }
    }

    fun setOnHeartRateChangedCallback(
        callback: (HeartRateMeasurement) -> Unit
    ) {
        onHeartRateChangedCallback = callback
    }

}