package com.healthmonitoring.wear.feature.skin_temperature.data.listener

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.consts.Timing
import com.healthmonitoring.wear.core.HealthTrackerProvider
import com.healthmonitoring.wear.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkinTemperatureListener @Inject constructor(
    private val healthTrackerProvider: HealthTrackerProvider
) {
    private var skinTemperatureTracker: HealthTracker? = null

    private val skinTemperatureHandler = Handler(Looper.getMainLooper())

    private var onSkinTemperatureChangedCallback:
            ((SkinTemperatureMeasurement) -> Unit)? = null

    private val flushRunnable = object : Runnable {
        override fun run() {
            flushSkinTemperatureData()

            skinTemperatureHandler.postDelayed(
                this,
                Timing.FLUSH_INTERVAL_MS
            )
        }
    }

    private val trackerEventListener = object : HealthTracker.TrackerEventListener {

        override fun onDataReceived(dataPoints: MutableList<DataPoint>) {
            dataPoints.forEach { dataPoint ->
                val status = dataPoint.getValue(
                    ValueKey.SkinTemperatureSet.STATUS
                )

                val objectTemperature = dataPoint.getValue(
                    ValueKey.SkinTemperatureSet.OBJECT_TEMPERATURE
                )

                val ambientTemperature = dataPoint.getValue(
                    ValueKey.SkinTemperatureSet.AMBIENT_TEMPERATURE
                )

                Log.i(
                    Tags.SKIN_TEMPERATURE_LISTENER,
                    "Object: $objectTemperature °C, ambient: $ambientTemperature °C, status: $status, timestamp: ${dataPoint.timestamp}"
                )

                if (status == NORMAL_MEASUREMENT_STATUS) {
                    val measurement = SkinTemperatureMeasurement(
                        objectTemperature = objectTemperature,
                        ambientTemperature = ambientTemperature,
                        status = status,
                        timestamp = dataPoint.timestamp
                    )

                    onSkinTemperatureChangedCallback?.invoke(measurement)
                }
            }
        }

        override fun onFlushCompleted() {
            Log.i(
                Tags.SKIN_TEMPERATURE_LISTENER,
                "Skin temperature tracker flush completed."
            )
        }

        override fun onError(error: HealthTracker.TrackerError) {
            Log.e(
                Tags.SKIN_TEMPERATURE_LISTENER,
                "Skin temperature tracker error: $error"
            )
        }
    }

    fun startTracking() {
        if (skinTemperatureTracker != null) {
            return
        }

        val tracker = healthTrackerProvider.getTracker(
            HealthTrackerType.SKIN_TEMPERATURE_CONTINUOUS
        )

        if (tracker == null) {
            Log.w(
                Tags.SKIN_TEMPERATURE_LISTENER,
                "Skin temperature tracker is not available."
            )

            return
        }

        skinTemperatureTracker = tracker

        skinTemperatureHandler.post {
            skinTemperatureTracker?.setEventListener(trackerEventListener)
            startPeriodicFlush()
        }
    }

    fun stopTracking() {
        skinTemperatureHandler.post {
            skinTemperatureTracker?.unsetEventListener()
            stopPeriodicFlush()
            skinTemperatureTracker = null
        }
    }

    fun setOnSkinTemperatureChangedCallback(
        callback: (SkinTemperatureMeasurement) -> Unit
    ) {
        onSkinTemperatureChangedCallback = callback
    }

    private fun startPeriodicFlush() {
        skinTemperatureHandler.removeCallbacks(flushRunnable)
        skinTemperatureHandler.postDelayed(
            flushRunnable,
            Timing.FLUSH_INTERVAL_MS
        )
    }

    private fun stopPeriodicFlush() {
        skinTemperatureHandler.removeCallbacks(flushRunnable)
    }

    private fun flushSkinTemperatureData() {
        try {
            Log.i(
                Tags.SKIN_TEMPERATURE_LISTENER,
                "Flushing skin temperature data."
            )

            skinTemperatureTracker?.flush()
        } catch (exception: Exception) {
            Log.e(
                Tags.SKIN_TEMPERATURE_LISTENER,
                "Skin temperature flush failed.",
                exception
            )
        }
    }

    companion object {
        private const val NORMAL_MEASUREMENT_STATUS = 0
    }
}