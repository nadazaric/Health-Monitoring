package com.healthmonitoring.wear.core.datalayer

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Asset
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurementSession
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PpgSessionSenderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PpgSessionSender {

    override fun sendSession(session: PpgMeasurementSession) {
        val sessionBytes = serializeSession(session)
        val sessionAsset = Asset.createFromBytes(sessionBytes)

        val putDataMapRequest = PutDataMapRequest.create(
            DataLayerConstants.PPG_SESSION_PATH_PREFIX + session.id
        ).apply {
            dataMap.putString(DataLayerConstants.SESSION_ID_KEY, session.id)
            dataMap.putLong(DataLayerConstants.STARTED_AT_KEY, session.startedAt)
            dataMap.putLong(DataLayerConstants.ENDED_AT_KEY, session.endedAt)
            dataMap.putLong(DataLayerConstants.UPDATED_AT_KEY, System.currentTimeMillis())
            dataMap.putAsset(DataLayerConstants.PPG_SESSION_ASSET_KEY, sessionAsset)
        }

        val putDataRequest = putDataMapRequest
            .asPutDataRequest()
            .setUrgent()

        Wearable.getDataClient(context)
            .putDataItem(putDataRequest)
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    "Failed to send PPG measurement session.",
                    exception
                )
            }
    }

    private fun serializeSession(session: PpgMeasurementSession): ByteArray {
        val samples = JSONArray()

        session.samples.forEach { sample ->
            samples.put(
                JSONObject().apply {
                    put(DataLayerConstants.TIMESTAMP_KEY, sample.timestamp)
                    put(DataLayerConstants.GREEN_KEY, sample.green)
                    put(DataLayerConstants.RED_KEY, sample.red)
                    put(DataLayerConstants.INFRARED_KEY, sample.infrared)
                    put(DataLayerConstants.PROCESSED_VALUE_KEY, sample.processedValue)
                    put(DataLayerConstants.BREATHING_PHASE_KEY, sample.breathingPhase.name)
                }
            )
        }

        val json = JSONObject().apply {
            put(DataLayerConstants.SESSION_ID_KEY, session.id)
            put(DataLayerConstants.STARTED_AT_KEY, session.startedAt)
            put(DataLayerConstants.ENDED_AT_KEY, session.endedAt)
            put(DataLayerConstants.SAMPLES_KEY, samples)
        }

        return json.toString().toByteArray(Charsets.UTF_8)
    }
}