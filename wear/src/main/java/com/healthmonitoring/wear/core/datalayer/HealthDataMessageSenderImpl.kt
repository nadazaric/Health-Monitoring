package com.healthmonitoring.wear.core.datalayer

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.feature.heart_rate.domain.model.HeartRateMeasurement
import com.healthmonitoring.wear.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2Measurement
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthDataMessageSenderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : HealthDataMessageSender {

    override fun sendHeartRateMeasurement(
        measurement: HeartRateMeasurement
    ) {
        sendHealthData(
            path = DataLayerConstants.HEART_RATE_LATEST_PATH,
            status = measurement.status,
            timestamp = measurement.timestamp,
            failureMessage = "Failed to update latest heart rate data item."
        ) {
            putInt(DataLayerConstants.BPM_KEY, measurement.bpm)
        }
    }

    override fun sendSkinTemperatureMeasurement(
        measurement: SkinTemperatureMeasurement
    ) {
        sendHealthData(
            path = DataLayerConstants.SKIN_TEMPERATURE_LATEST_PATH,
            status = measurement.status,
            timestamp = measurement.timestamp,
            failureMessage = "Failed to update latest skin temperature data item."
        ) {
            putFloat(DataLayerConstants.OBJECT_TEMPERATURE_KEY, measurement.objectTemperature)
            putFloat(DataLayerConstants.AMBIENT_TEMPERATURE_KEY, measurement.ambientTemperature)
        }
    }

    override fun sendSpO2Measurement(
        measurement: SpO2Measurement
    ) {
        sendHealthData(
            path = DataLayerConstants.SPO2_LATEST_PATH,
            status = measurement.status,
            timestamp = measurement.timestamp,
            failureMessage = "Failed to update latest SpO2 data item."
        ) {
            putInt(DataLayerConstants.SPO2_KEY, measurement.spo2)
            putInt(DataLayerConstants.SPO2_HEART_RATE_KEY, measurement.heartRate)
        }
    }

    private fun sendHealthData(
        path: String,
        status: Int,
        timestamp: Long,
        failureMessage: String,
        addSensorValues: DataMap.() -> Unit
    ) {
        val putDataMapRequest = PutDataMapRequest.create(path).apply {
            dataMap.apply {
                addSensorValues()
                putInt(DataLayerConstants.STATUS_KEY, status)
                putLong(DataLayerConstants.TIMESTAMP_KEY, timestamp)
                putLong(DataLayerConstants.UPDATED_AT_KEY, System.currentTimeMillis())
            }
        }

        val putDataRequest = putDataMapRequest
            .asPutDataRequest()
            .setUrgent()

        Wearable.getDataClient(context)
            .putDataItem(putDataRequest)
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    failureMessage,
                    exception
                )
            }
    }
}