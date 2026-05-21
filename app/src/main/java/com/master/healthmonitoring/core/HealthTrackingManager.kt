package com.master.healthmonitoring.core

import android.app.Activity
import android.util.Log
import com.samsung.android.service.health.tracking.ConnectionListener
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.HealthTrackerException
import com.samsung.android.service.health.tracking.HealthTrackingService
import com.samsung.android.service.health.tracking.data.HealthTrackerType

class HealthTrackingManager(
    private val activity: Activity
) {
    private val applicationContext = activity.applicationContext
    private var healthTrackingService: HealthTrackingService? = null
    private var connected = false

    private val connectionListener = object : ConnectionListener {

        override fun onConnectionSuccess() {
            connected = true

            Log.i(TAG, "Health Tracking Service connected successfully.")

            logSupportedTrackers()
        }

        override fun onConnectionEnded() {
            connected = false

            Log.i(TAG, "Health Tracking Service connection ended.")
        }

        override fun onConnectionFailed(exception: HealthTrackerException) {
            connected = false

            val errorMessage = exception.message ?: "Unknown error"

            Log.e(
                TAG,
                "Health Tracking Service connection failed. Error code: ${exception.errorCode}. Message: $errorMessage",
                exception
            )

            if (exception.hasResolution()) {
                Log.i(TAG, "Trying to resolve Health Tracking Service connection issue.")

                exception.resolve(activity)
            }
        }
    }

    fun connect() {
        if (connected) {
            Log.i(TAG, "Health Tracking Service is already connected.")
            return
        }

        if (healthTrackingService == null) {
            Log.i(TAG, "Creating Health Tracking Service instance.")

            healthTrackingService = HealthTrackingService(
                connectionListener,
                applicationContext
            )
        }

        Log.i(TAG, "Connecting to Health Tracking Service.")

        healthTrackingService?.connectService()
    }

    fun disconnect() {
        Log.i(TAG, "Disconnecting from Health Tracking Service.")

        healthTrackingService?.disconnectService()
        healthTrackingService = null
        connected = false
    }

    fun isConnected(): Boolean {
        return connected
    }

    fun isTrackerAvailable(healthTrackerType: HealthTrackerType): Boolean {
        val supportedTrackers = healthTrackingService
            ?.getTrackingCapability()
            ?.getSupportHealthTrackerTypes()
            .orEmpty()

        val available = supportedTrackers.contains(healthTrackerType)

        Log.i(
            TAG,
            "Tracker availability checked. Tracker: $healthTrackerType, available: $available"
        )

        return available
    }

    fun getTracker(healthTrackerType: HealthTrackerType): HealthTracker? {
        if (!connected) {
            Log.w(
                TAG,
                "Cannot get tracker because Health Tracking Service is not connected. Tracker: $healthTrackerType"
            )

            return null
        }

        if (!isTrackerAvailable(healthTrackerType)) {
            Log.w(
                TAG,
                "Requested tracker is not available on this device. Tracker: $healthTrackerType"
            )

            return null
        }

        return try {
            Log.i(TAG, "Creating Health Tracker instance. Tracker: $healthTrackerType")

            healthTrackingService?.getHealthTracker(healthTrackerType)
        } catch (exception: Exception) {
            Log.e(
                TAG,
                "Failed to create Health Tracker instance. Tracker: $healthTrackerType",
                exception
            )

            null
        }
    }

    private fun logSupportedTrackers() {
        val supportedTrackers = healthTrackingService
            ?.getTrackingCapability()
            ?.getSupportHealthTrackerTypes()
            .orEmpty()

        Log.i(TAG, "Supported tracker count: ${supportedTrackers.size}")

        supportedTrackers.forEach { trackerType ->
            Log.i(TAG, "Supported tracker: $trackerType")
        }
    }

    companion object {
        private const val TAG = "HealthTrackingManager"
    }
}