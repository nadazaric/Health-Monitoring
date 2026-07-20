package com.healthmonitoring.mobile.feature.spo2.domain.repository

import com.healthmonitoring.mobile.feature.spo2.domain.model.SpO2Measurement
import kotlinx.coroutines.flow.Flow

interface SpO2Repository {
    fun observeSpO2(): Flow<SpO2Measurement>
}