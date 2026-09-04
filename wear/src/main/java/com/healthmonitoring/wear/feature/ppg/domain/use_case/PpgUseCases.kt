package com.healthmonitoring.wear.feature.ppg.domain.use_case

import javax.inject.Inject

data class PpgUseCases @Inject constructor(
    val observePpg: ObservePpgUseCase,
    val observeMeasurementErrors: ObservePpgMeasurementErrorsUseCase,
    val startPpgMeasurement: StartPpgMeasurementUseCase,
    val stopPpgMeasurement: StopPpgMeasurementUseCase,
    val startPpgSession: StartPpgSessionUseCase,
    val addProcessedSampleToPpgSession: AddProcessedSampleToPpgSessionUseCase,
    val finishPpgSession: FinishPpgSessionUseCase,
    val resetPpgSession: ResetPpgSessionUseCase
)