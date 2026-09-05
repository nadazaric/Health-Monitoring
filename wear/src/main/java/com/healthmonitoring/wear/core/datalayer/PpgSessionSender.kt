package com.healthmonitoring.wear.core.datalayer

import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurementSession

interface PpgSessionSender {

    fun sendSession(session: PpgMeasurementSession)
}