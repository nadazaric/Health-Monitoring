package com.healthmonitoring.wear.core.datalayer

object DataLayerConstants {
    const val HEART_RATE_LATEST_PATH = "/heart-rate/latest"
    const val SKIN_TEMPERATURE_LATEST_PATH = "/skin-temperature/latest"
    const val SPO2_LATEST_PATH = "/spo2/latest"
    const val PPG_SESSION_PATH_PREFIX = "/ppg/session/"

    const val BPM_KEY = "bpm"
    const val OBJECT_TEMPERATURE_KEY = "objectTemperature"
    const val AMBIENT_TEMPERATURE_KEY = "ambientTemperature"
    const val SPO2_KEY = "spo2"
    const val SPO2_HEART_RATE_KEY = "spo2HeartRate"
    const val PPG_SESSION_ASSET_KEY = "ppgSession"
    const val SAMPLES_KEY = "samples"

    const val STATUS_KEY = "status"
    const val TIMESTAMP_KEY = "timestamp"
    const val UPDATED_AT_KEY = "updatedAt"
    const val SESSION_ID_KEY = "sessionId"
    const val STARTED_AT_KEY = "startedAt"
    const val ENDED_AT_KEY = "endedAt"

    // JSON Keys
    const val GREEN_KEY = "green"
    const val RED_KEY = "red"
    const val INFRARED_KEY = "infrared"
    const val PROCESSED_VALUE_KEY = "processedValue"
    const val BREATHING_PHASE_KEY = "breathingPhase"
}