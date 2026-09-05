package com.healthmonitoring.mobile.core.datalayer

import com.google.android.gms.wearable.DataItem

interface PpgSessionReceiver {

    fun receiveSession(dataItem: DataItem)
}