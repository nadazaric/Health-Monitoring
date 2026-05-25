package com.master.healthmonitoring.core

import android.content.Context
import android.util.Log
import com.master.healthmonitoring.consts.Tags
import com.samsung.android.service.health.tracking.ConnectionListener
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.HealthTrackerException
import com.samsung.android.service.health.tracking.HealthTrackingService
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthTrackerProviderImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : HealthTrackerProvider {
    private var healthTrackingService: HealthTrackingService? = null
    private var connected = false

    var onConnected: (() -> Unit)? = null

    private val connectionListener = object : ConnectionListener {

        override fun onConnectionSuccess() {
            connected = true

            Log.d(Tags.HEALTH_TRACKING_MANAGER, "Health Tracking Service connected successfully.")

            logSupportedTrackers()

            onConnected?.invoke()
        }

        override fun onConnectionEnded() {
            connected = false

            Log.i(Tags.HEALTH_TRACKING_MANAGER, "Health Tracking Service connection ended.")
        }

        override fun onConnectionFailed(exception: HealthTrackerException) {
            connected = false

            val errorMessage = exception.message ?: "Unknown error"

            Log.e(
                Tags.HEALTH_TRACKING_MANAGER,
                "Health Tracking Service connection failed. Error code: ${exception.errorCode}. Message: $errorMessage",
                exception
            )

            if (exception.hasResolution()) {
                Log.i(
                    Tags.HEALTH_TRACKING_MANAGER,
                    "Trying to resolve Health Tracking Service connection issue."
                )
            }
        }
    }

    override fun connect() {
        if (connected) {
            Log.i(Tags.HEALTH_TRACKING_MANAGER, "Health Tracking Service is already connected.")
            return
        }

        if (healthTrackingService == null) {
            healthTrackingService = HealthTrackingService(
                connectionListener,
                applicationContext
            )
        }

        healthTrackingService?.connectService()
    }

    override fun disconnect() {
        Log.i(Tags.HEALTH_TRACKING_MANAGER, "Disconnecting from Health Tracking Service.")

        healthTrackingService?.disconnectService()
        healthTrackingService = null
        connected = false
    }

    override fun isConnected(): Boolean {
        return connected
    }

    override fun isTrackerAvailable(healthTrackerType: HealthTrackerType): Boolean {
        val supportedTrackers = healthTrackingService
            ?.trackingCapability
            ?.supportHealthTrackerTypes
            .orEmpty()

        val available = supportedTrackers.contains(healthTrackerType)

        Log.i(
            Tags.HEALTH_TRACKING_MANAGER,
            "Tracker availability checked. Tracker: $healthTrackerType, available: $available"
        )

        return available
    }

    override fun getTracker(healthTrackerType: HealthTrackerType): HealthTracker? {
        if (!connected) {
            Log.w(
                Tags.HEALTH_TRACKING_MANAGER,
                "Cannot get tracker because Health Tracking Service is not connected. Tracker: $healthTrackerType"
            )

            return null
        }

        if (!isTrackerAvailable(healthTrackerType)) {
            Log.w(
                Tags.HEALTH_TRACKING_MANAGER,
                "Requested tracker is not available on this device. Tracker: $healthTrackerType"
            )

            return null
        }

        return try {
            Log.i(
                Tags.HEALTH_TRACKING_MANAGER,
                "Creating Health Tracker instance. Tracker: $healthTrackerType"
            )

            healthTrackingService?.getHealthTracker(healthTrackerType)
        } catch (exception: Exception) {
            Log.e(
                Tags.HEALTH_TRACKING_MANAGER,
                "Failed to create Health Tracker instance. Tracker: $healthTrackerType",
                exception
            )

            null
        }
    }

    private fun logSupportedTrackers() {
        val supportedTrackers = healthTrackingService
            ?.trackingCapability
            ?.supportHealthTrackerTypes
            .orEmpty()

        Log.i(Tags.HEALTH_TRACKING_MANAGER, "Supported tracker count: ${supportedTrackers.size}")

        supportedTrackers.forEach { trackerType ->
            Log.i(Tags.HEALTH_TRACKING_MANAGER, "Supported tracker: $trackerType")
        }
    }

}