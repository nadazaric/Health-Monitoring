package com.healthmonitoring.wear.core.datalayer

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.feature.heartrate.domain.model.HeartRateMeasurement
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
}