package com.master.healthmonitoring.feature.heartrate.presentation

sealed class HeartRateEvent {
    object StartTracking : HeartRateEvent()
    object StopTracking : HeartRateEvent()
    object PermissionDenied : HeartRateEvent()
}