package com.healthmonitoring.wear.core

import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.HealthTrackerType

interface HealthTrackerProvider {

    fun connect()

    fun disconnect()

    fun isConnected(): Boolean

    fun isTrackerAvailable(healthTrackerType: HealthTrackerType): Boolean

    fun getTracker(healthTrackerType: HealthTrackerType): HealthTracker?

    fun setOnConnectedCallback(callback: () -> Unit)

}