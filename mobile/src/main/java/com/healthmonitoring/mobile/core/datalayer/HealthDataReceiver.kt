package com.healthmonitoring.mobile.core.datalayer

import com.healthmonitoring.mobile.core.datalayer.model.HeartRateMeasurement

interface HealthDataReceiver {
    fun startListening(
        onHeartRateReceived: (HeartRateMeasurement) -> Unit,
        onNoHeartRateDataFound: () -> Unit
    )

    fun stopListening()
}