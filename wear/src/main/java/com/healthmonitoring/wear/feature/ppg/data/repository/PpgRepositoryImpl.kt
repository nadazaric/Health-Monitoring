package com.healthmonitoring.wear.feature.ppg.data.repository

import android.util.Log
import com.healthmonitoring.wear.consts.Tags
import com.healthmonitoring.wear.feature.ppg.data.listener.PpgListener
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgRawSample
import com.healthmonitoring.wear.feature.ppg.domain.repository.PpgRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PpgRepositoryImpl @Inject constructor(
    private val ppgListener: PpgListener
) : PpgRepository {

    private val ppgMeasurements =
        MutableSharedFlow<PpgRawSample>(
            replay = 1,
            extraBufferCapacity = 100
        )

    private val measurementErrors =
        MutableSharedFlow<String>(
            replay = 0,
            extraBufferCapacity = 1
        )

    init {
        ppgListener.setOnPpgMeasuredCallback { measurement ->
            ppgMeasurements.tryEmit(measurement)
        }

        ppgListener.setOnMeasurementFailedCallback { message ->
            measurementErrors.tryEmit(message)
        }
    }

    override fun observePpg(): Flow<PpgRawSample> {
        return ppgMeasurements.asSharedFlow()
    }

    override fun observeMeasurementErrors(): Flow<String> {
        return measurementErrors.asSharedFlow()
    }

    override fun startMeasurement() {
        Log.d(
            Tags.PPG_REPOSITORY,
            "Starting PPG measurement."
        )

        ppgListener.startMeasurement()
    }

    override fun stopMeasurement() {
        Log.d(
            Tags.PPG_REPOSITORY,
            "Stopping PPG measurement."
        )

        ppgListener.stopMeasurement()
    }
}