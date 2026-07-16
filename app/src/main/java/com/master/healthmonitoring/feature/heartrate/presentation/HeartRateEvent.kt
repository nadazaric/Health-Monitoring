package com.master.healthmonitoring.feature.heartrate.presentation

sealed class HeartRateEvent {
    data object MonitoringStarted : HeartRateEvent()
    data object MonitoringStopped : HeartRateEvent()
    data object PermissionDenied : HeartRateEvent()
}