package com.healthmonitoring.mobile.feature.spo2.data.repository

import com.healthmonitoring.mobile.core.datalayer.HealthDataReceiver
import com.healthmonitoring.mobile.feature.spo2.domain.model.SpO2Measurement
import com.healthmonitoring.mobile.feature.spo2.domain.repository.SpO2Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpO2RepositoryImpl @Inject constructor(
    private val healthDataReceiver: HealthDataReceiver
) : SpO2Repository {

    override fun observeSpO2(): Flow<SpO2Measurement> {
        return healthDataReceiver.observeSpO2()
    }
}