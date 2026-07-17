package com.healthmonitoring.mobile.core.datalayer

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.healthmonitoring.mobile.consts.Tags
import com.healthmonitoring.mobile.feature.heartrate.domain.model.HeartRateMeasurement
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthDataReceiverImpl @Inject constructor(
    @ApplicationContext context: Context
) : HealthDataReceiver, DataClient.OnDataChangedListener {

    private val applicationContext = context.applicationContext

    private val heartRateMeasurements = MutableSharedFlow<HeartRateMeasurement>(
        replay = 1
    )

    private var isListening = false

    override fun observeHeartRate(): Flow<HeartRateMeasurement> {
        return heartRateMeasurements.asSharedFlow()
    }

    override fun startListening() {
        if (isListening) {
            Log.d(Tags.DATA_LAYER, "Health data receiver is already listening.")
            return
        }

        isListening = true

        Wearable.getDataClient(applicationContext).addListener(this)

        Log.d(Tags.DATA_LAYER, "Health data receiver listener registered.")

        readLatestHeartRate()
    }

    override fun stopListening() {
        if (!isListening) {
            return
        }

        Wearable.getDataClient(applicationContext).removeListener(this)

        isListening = false

        Log.d(Tags.DATA_LAYER, "Health data receiver listener removed.")
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        try {
            dataEvents.forEach { dataEvent ->
                if (dataEvent.type == DataEvent.TYPE_CHANGED) {
                    handleHeartRateDataItem(dataEvent.dataItem)
                }
            }
        } finally {
            dataEvents.release()
        }
    }

    private fun readLatestHeartRate() {
        Wearable.getDataClient(applicationContext)
            .getDataItems()
            .addOnSuccessListener { dataItems ->
                try {
                    dataItems.forEach { dataItem ->
                        handleHeartRateDataItem(dataItem)
                    }
                } finally {
                    dataItems.release()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    "Failed to read latest heart rate data item.",
                    exception
                )
            }
    }

    private fun handleHeartRateDataItem(dataItem: DataItem) {
        if (dataItem.uri.path != DataLayerConstants.HEART_RATE_LATEST_PATH) {
            return
        }

        val dataMap = DataMapItem.fromDataItem(dataItem).dataMap

        val measurement = HeartRateMeasurement(
            bpm = dataMap.getInt(DataLayerConstants.BPM_KEY),
            status = dataMap.getInt(DataLayerConstants.STATUS_KEY),
            timestamp = dataMap.getLong(DataLayerConstants.TIMESTAMP_KEY)
        )

        val emitted = heartRateMeasurements.tryEmit(measurement)

        Log.d(
            Tags.DATA_LAYER,
            "BPM: ${measurement.bpm}, emitted: $emitted"
        )
    }
}