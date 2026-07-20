package com.healthmonitoring.wear.core.datalayer

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.feature.heart_rate.domain.model.HeartRateMeasurement
import com.healthmonitoring.wear.feature.skin_temperature.domain.model.SkinTemperatureMeasurement
import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2Measurement
import com.healthmonitoring.wear.feature.spo2.domain.model.SpO2MeasurementState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthDataMessageSenderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : HealthDataMessageSender {

    override fun sendHeartRateMeasurement(measurement: HeartRateMeasurement) {
        val putDataMapRequest = PutDataMapRequest.create(
            DataLayerConstants.HEART_RATE_LATEST_PATH
        ).apply {
            dataMap.putInt(DataLayerConstants.BPM_KEY, measurement.bpm)
            dataMap.putInt(DataLayerConstants.STATUS_KEY, measurement.status)
            dataMap.putLong(DataLayerConstants.TIMESTAMP_KEY, measurement.timestamp)
            dataMap.putLong(DataLayerConstants.UPDATED_AT_KEY, System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest
            .asPutDataRequest()
            .setUrgent()

        Wearable.getDataClient(context)
            .putDataItem(putDataRequest)
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    "Failed to update latest heart rate data item.",
                    exception
                )
            }
    }

    override fun sendSkinTemperatureMeasurement(
        measurement: SkinTemperatureMeasurement
    ) {
        val putDataMapRequest = PutDataMapRequest.create(
            DataLayerConstants.SKIN_TEMPERATURE_LATEST_PATH
        ).apply {
            dataMap.putFloat(DataLayerConstants.OBJECT_TEMPERATURE_KEY, measurement.objectTemperature)
            dataMap.putFloat(DataLayerConstants.AMBIENT_TEMPERATURE_KEY, measurement.ambientTemperature)
            dataMap.putInt(DataLayerConstants.STATUS_KEY, measurement.status)
            dataMap.putLong(DataLayerConstants.TIMESTAMP_KEY, measurement.timestamp)
            dataMap.putLong(DataLayerConstants.UPDATED_AT_KEY, System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest
            .asPutDataRequest()
            .setUrgent()

        Wearable.getDataClient(context)
            .putDataItem(putDataRequest)
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    "Failed to update latest skin temperature data item.",
                    exception
                )
            }
    }

    override fun sendSpO2Measurement(
        measurement: SpO2Measurement
    ) {
        val putDataMapRequest = PutDataMapRequest.create(
            DataLayerConstants.SPO2_LATEST_PATH
        ).apply {
            dataMap.putInt(DataLayerConstants.SPO2_KEY, measurement.spo2)
            dataMap.putInt(DataLayerConstants.SPO2_HEART_RATE_KEY, measurement.heartRate)
            dataMap.putInt(DataLayerConstants.STATUS_KEY, measurement.status)
            dataMap.putLong(DataLayerConstants.TIMESTAMP_KEY, measurement.timestamp)
            dataMap.putLong(DataLayerConstants.UPDATED_AT_KEY, System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest
            .asPutDataRequest()
            .setUrgent()

        Wearable.getDataClient(context)
            .putDataItem(putDataRequest)
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    "Failed to update latest SpO2 data item.",
                    exception
                )
            }
    }

    override fun sendSpO2MeasurementState(
        measurementState: SpO2MeasurementState,
        errorMessage: String?
    ) {
        val putDataMapRequest = PutDataMapRequest.create(
            DataLayerConstants.SPO2_STATE_PATH
        ).apply {
            dataMap.putString(DataLayerConstants.MEASUREMENT_STATE_KEY, measurementState.dataLayerValue)
            dataMap.putString(DataLayerConstants.ERROR_MESSAGE_KEY, errorMessage.orEmpty())
            dataMap.putLong(DataLayerConstants.UPDATED_AT_KEY, System.currentTimeMillis())
        }

        val putDataRequest = putDataMapRequest
            .asPutDataRequest()
            .setUrgent()

        Wearable.getDataClient(context)
            .putDataItem(putDataRequest)
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    "Failed to update latest SpO2 measurement state.",
                    exception
                )
            }
    }
}