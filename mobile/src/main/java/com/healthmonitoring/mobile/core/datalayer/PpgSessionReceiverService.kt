package com.healthmonitoring.mobile.core.datalayer

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PpgSessionReceiverService : WearableListenerService() {

    @Inject
    lateinit var ppgSessionReceiver: PpgSessionReceiver

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type != DataEvent.TYPE_CHANGED) {
                return@forEach
            }

            val dataItem = event.dataItem
            val path = dataItem.uri.path ?: return@forEach

            if (!path.startsWith(DataLayerConstants.PPG_SESSION_PATH_PREFIX)) {
                return@forEach
            }

            ppgSessionReceiver.receiveSession(dataItem)
        }
    }
}