package com.healthmonitoring.wear.feature.spo2.data.listener

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.core.HealthTrackerProvider
import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2Measurement
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpO2Listener @Inject constructor(
    private val healthTrackerProvider: HealthTrackerProvider
) {
    private var spO2Tracker: HealthTracker? = null

    private val spO2Handler = Handler(Looper.getMainLooper())

    private var onSpO2MeasuredCallback: ((SpO2Measurement) -> Unit)? = null

    private var onMeasurementFailedCallback: ((String) -> Unit)? = null

    private val trackerEventListener = object : HealthTracker.TrackerEventListener {

        override fun onDataReceived(dataPoints: MutableList<DataPoint>) {
            dataPoints.forEach { dataPoint ->
                val status = dataPoint.getValue(ValueKey.SpO2Set.STATUS)
                val spO2 = dataPoint.getValue(ValueKey.SpO2Set.SPO2)
                val heartRate = dataPoint.getValue(ValueKey.SpO2Set.HEART_RATE)

                Log.i(
                    Tags.SPO2_LISTENER,
                    "SpO2: $spO2%, heart rate: $heartRate BPM, status: $status, timestamp: ${dataPoint.timestamp}"
                )

                when (status) {
                    MEASUREMENT_COMPLETED_STATUS -> {
                        val measurement = SpO2Measurement(
                            spo2 = spO2,
                            heartRate = heartRate,
                            status = status,
                            timestamp = dataPoint.timestamp
                        )

                        onSpO2MeasuredCallback?.invoke(measurement)
                        stopMeasurement()
                    }

                    MEASUREMENT_CALCULATING_STATUS -> {
                        Log.d(
                            Tags.SPO2_LISTENER,
                            "SpO2 measurement is still calculating."
                        )
                    }

                    DEVICE_MOVED_STATUS -> {
                        handleMeasurementFailure(
                            "Device moved during SpO2 measurement."
                        )
                    }

                    LOW_SIGNAL_STATUS -> {
                        handleMeasurementFailure(
                            "SpO2 signal quality is too low."
                        )
                    }

                    TIMEOUT_STATUS -> {
                        handleMeasurementFailure(
                            "SpO2 measurement timed out."
                        )
                    }

                    else -> {
                        Log.w(
                            Tags.SPO2_LISTENER,
                            "Unknown SpO2 measurement status: $status"
                        )
                    }
                }
            }
        }

        override fun onFlushCompleted() {
            Log.d(
                Tags.SPO2_LISTENER,
                "SpO2 tracker flush completed."
            )
        }

        override fun onError(error: HealthTracker.TrackerError) {
            handleMeasurementFailure(
                "SpO2 tracker error: $error"
            )
        }
    }

    fun startMeasurement() {
        if (spO2Tracker != null) {
            return
        }

        val tracker = healthTrackerProvider.getTracker(
            HealthTrackerType.SPO2_ON_DEMAND
        )

        if (tracker == null) {
            handleMeasurementFailure(
                "SpO2 tracker is not available."
            )

            return
        }

        spO2Tracker = tracker

        spO2Handler.post {
            spO2Tracker?.setEventListener(trackerEventListener)
        }
    }

    fun stopMeasurement() {
        spO2Handler.post {
            Log.i(
                Tags.SPO2_LISTENER,
                "Stopping SpO2 measurement."
            )

            spO2Tracker?.unsetEventListener()
            spO2Tracker = null
        }
    }

    fun setOnSpO2MeasuredCallback(
        callback: (SpO2Measurement) -> Unit
    ) {
        onSpO2MeasuredCallback = callback
    }

    fun setOnMeasurementFailedCallback(
        callback: (String) -> Unit
    ) {
        onMeasurementFailedCallback = callback
    }

    private fun handleMeasurementFailure(message: String) {
        Log.e(
            Tags.SPO2_LISTENER,
            message
        )

        onMeasurementFailedCallback?.invoke(message)
        stopMeasurement()
    }

    companion object {
        private const val MEASUREMENT_CALCULATING_STATUS = 0
        private const val MEASUREMENT_COMPLETED_STATUS = 2
        private const val DEVICE_MOVED_STATUS = -4
        private const val LOW_SIGNAL_STATUS = -5
        private const val TIMEOUT_STATUS = -6
    }
}