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
import com.healthmonitoring.mobile.core.datalayer.model.HeartRateMeasurement
import javax.inject.Singleton
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class HealthDataReceiverImpl @Inject constructor(
    @ApplicationContext context: Context
) : HealthDataReceiver, DataClient.OnDataChangedListener {

    private val applicationContext = context.applicationContext

    private var onHeartRateReceived: ((HeartRateMeasurement) -> Unit)? = null
    private var onNoHeartRateDataFound: (() -> Unit)? = null

    override fun startListening(
        onHeartRateReceived: (HeartRateMeasurement) -> Unit,
        onNoHeartRateDataFound: () -> Unit
    ) {
        this.onHeartRateReceived = onHeartRateReceived
        this.onNoHeartRateDataFound = onNoHeartRateDataFound

        Wearable.getDataClient(applicationContext).addListener(this)

        Log.d(Tags.DATA_LAYER, "Data layer live listener registered.")

        readLatestHeartRate()
    }

    override fun stopListening() {
        Wearable.getDataClient(applicationContext).removeListener(this)

        Log.d(Tags.DATA_LAYER, "Data layer live listener removed.")

        onHeartRateReceived = null
        onNoHeartRateDataFound = null
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
                    var latestValueFound = false

                    dataItems.forEach { dataItem ->
                        if (dataItem.uri.path == DataLayerConstants.HEART_RATE_LATEST_PATH) {
                            handleHeartRateDataItem(dataItem)
                            latestValueFound = true
                        }
                    }

                    if (!latestValueFound) {
                        onNoHeartRateDataFound?.invoke()
                    }
                } finally {
                    dataItems.release()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(Tags.DATA_LAYER, "Failed to read latest heart rate data item.", exception)
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

        Log.d(
            Tags.DATA_LAYER,
            "BPM: ${measurement.bpm}, status: ${measurement.status}, timestamp: ${measurement.timestamp}"
        )

        onHeartRateReceived?.invoke(measurement)
    }
}