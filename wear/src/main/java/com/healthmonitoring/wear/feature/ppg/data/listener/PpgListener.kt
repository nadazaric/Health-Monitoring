package com.healthmonitoring.wear.feature.ppg.data.listener

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.core.HealthTrackerProvider
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurement
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.PpgType
import com.samsung.android.service.health.tracking.data.ValueKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PpgListener @Inject constructor(
    private val healthTrackerProvider: HealthTrackerProvider
) {
    private var ppgTracker: HealthTracker? = null

    private val ppgHandler = Handler(Looper.getMainLooper())

    private var onPpgMeasuredCallback:
            ((PpgMeasurement) -> Unit)? = null

    private var onMeasurementFailedCallback:
            ((String) -> Unit)? = null

    private val trackerEventListener = object : HealthTracker.TrackerEventListener {

        override fun onDataReceived(dataPoints: MutableList<DataPoint>) {
            dataPoints.forEach { dataPoint ->
                val green = dataPoint.getValue(ValueKey.PpgSet.PPG_GREEN)
                val greenStatus = dataPoint.getValue(ValueKey.PpgSet.GREEN_STATUS)
                val red = dataPoint.getValue(ValueKey.PpgSet.PPG_RED)
                val redStatus = dataPoint.getValue(ValueKey.PpgSet.RED_STATUS)
                val infrared = dataPoint.getValue(ValueKey.PpgSet.PPG_IR)
                val infraredStatus = dataPoint.getValue(ValueKey.PpgSet.IR_STATUS)

                Log.i(
                    Tags.PPG_LISTENER,
                    "Green: $green, green status: $greenStatus, " +
                            "red: $red, red status: $redStatus, " +
                            "infrared: $infrared, " +
                            "infrared status: $infraredStatus, " +
                            "timestamp: ${dataPoint.timestamp}"
                )

                val measurement = PpgMeasurement(
                    green = green,
                    red = red,
                    infrared = infrared,
                    greenStatus = greenStatus,
                    redStatus = redStatus,
                    infraredStatus = infraredStatus,
                    timestamp = dataPoint.timestamp
                )

                onPpgMeasuredCallback?.invoke(measurement)
            }
        }

        override fun onFlushCompleted() {
            Log.d(Tags.PPG_LISTENER, "PPG tracker flush completed.")
        }

        override fun onError(error: HealthTracker.TrackerError) {
            handleMeasurementFailure("PPG tracker error: $error")
        }
    }

    fun startMeasurement() {
        if (ppgTracker != null) {
            return
        }

        val tracker = healthTrackerProvider.getPpgTracker(
            healthTrackerType = HealthTrackerType.PPG_ON_DEMAND,
            ppgTypes = setOf(
                PpgType.GREEN,
                PpgType.RED,
                PpgType.IR
            )
        )

        if (tracker == null) {
            handleMeasurementFailure("PPG tracker is not available.")

            return
        }

        ppgTracker = tracker

        ppgHandler.post {
            ppgTracker?.setEventListener(trackerEventListener)
        }
    }

    fun stopMeasurement() {
        ppgHandler.post {
            Log.i(Tags.PPG_LISTENER, "Stopping PPG measurement.")

            ppgTracker?.unsetEventListener()
            ppgTracker = null
        }
    }

    fun setOnPpgMeasuredCallback(
        callback: (PpgMeasurement) -> Unit
    ) {
        onPpgMeasuredCallback = callback
    }

    fun setOnMeasurementFailedCallback(
        callback: (String) -> Unit
    ) {
        onMeasurementFailedCallback = callback
    }

    private fun handleMeasurementFailure(message: String) {
        Log.e(Tags.PPG_LISTENER, message)

        onMeasurementFailedCallback?.invoke(message)
        stopMeasurement()
    }
}