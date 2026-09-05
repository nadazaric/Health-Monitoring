package com.healthmonitoring.mobile.core.datalayer

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.DataMapItem
import com.healthmonitoring.mobile.consts.Tags
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.google.android.gms.wearable.Wearable
import com.healthmonitoring.mobile.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgMeasurementSession
import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgSessionSample
import com.healthmonitoring.mobile.feature.ppg.domain.repository.PpgSessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject

@Singleton
class PpgSessionReceiverImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ppgSessionRepository: PpgSessionRepository
) : PpgSessionReceiver {

    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun receiveSession(dataItem: DataItem) {
        val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
        val asset = dataMap.getAsset(DataLayerConstants.PPG_SESSION_ASSET_KEY)

        if (asset == null) {
            Log.e(Tags.DATA_LAYER, "PPG session asset is missing.")
            return
        }

        Wearable.getDataClient(context)
            .getFdForAsset(asset)
            .addOnSuccessListener { assetFileDescriptor ->
                val bytes = assetFileDescriptor.inputStream.use { inputStream ->
                    inputStream.readBytes()
                }

                try {
                    val session = deserializeSession(bytes)

                    Log.d(
                        Tags.DATA_LAYER,
                        """
                        PPG session deserialized
                        id=${session.id}
                        startedAt=${session.startedAt}
                        endedAt=${session.endedAt}
                        samples=${session.samples.size}
                        firstSample=${session.samples.firstOrNull()}
                        lastSample=${session.samples.lastOrNull()}
                        """.trimIndent()
                    )

                    receiverScope.launch {
                        try {
                            ppgSessionRepository.saveSession(session)

                            Log.d(
                                Tags.DATA_LAYER,
                                "PPG session saved: ${session.id}"
                            )
                        } catch (exception: Exception) {
                            Log.e(
                                Tags.DATA_LAYER,
                                "Failed to save PPG session.",
                                exception
                            )
                        }
                    }
                } catch (exception: Exception) {
                    Log.e(
                        Tags.DATA_LAYER,
                        "Failed to deserialize PPG session.",
                        exception
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.e(
                    Tags.DATA_LAYER,
                    "Failed to read PPG session asset.",
                    exception
                )
            }
    }

    private fun deserializeSession(bytes: ByteArray): PpgMeasurementSession {
        val json = JSONObject(bytes.toString(Charsets.UTF_8))
        val samplesJson = json.getJSONArray(DataLayerConstants.SAMPLES_KEY)

        val samples = mutableListOf<PpgSessionSample>()

        for (index in 0 until samplesJson.length()) {
            val sampleJson = samplesJson.getJSONObject(index)

            samples.add(
                PpgSessionSample(
                    timestamp = sampleJson.getLong(DataLayerConstants.TIMESTAMP_KEY),
                    green = sampleJson.getInt(DataLayerConstants.GREEN_KEY),
                    red = sampleJson.getInt(DataLayerConstants.RED_KEY),
                    infrared = sampleJson.getInt(DataLayerConstants.INFRARED_KEY),
                    processedValue = sampleJson.getDouble(DataLayerConstants.PROCESSED_VALUE_KEY),
                    breathingPhase = PpgBreathingPhase.valueOf(sampleJson.getString(DataLayerConstants.BREATHING_PHASE_KEY))
                )
            )
        }

        return PpgMeasurementSession(
            id = json.getString(DataLayerConstants.SESSION_ID_KEY),
            startedAt = json.getLong(DataLayerConstants.STARTED_AT_KEY),
            endedAt = json.getLong(DataLayerConstants.ENDED_AT_KEY),
            samples = samples
        )
    }
}