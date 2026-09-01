package com.healthmonitoring.wear.feature.ppg.data.session

import android.util.Log
import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgMeasurementSession
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedSample
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgRawSample
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgSessionSample
import com.healthmonitoring.wear.feature.ppg.domain.session.PpgSessionCollector
import java.util.UUID
import javax.inject.Inject

class PpgSessionCollectorImpl @Inject constructor() : PpgSessionCollector {

    private data class PendingSample(
        val rawSample: PpgRawSample,
        val breathingPhase: PpgBreathingPhase
    )

    private val pendingSamples = mutableMapOf<Long, PendingSample>()
    private val sessionSamples = mutableListOf<PpgSessionSample>()

    private var sessionId: String? = null
    private var startedAt: Long? = null


    override fun start(startedAt: Long) {
        reset()

        sessionId = UUID.randomUUID().toString()
        this.startedAt = startedAt
    }

    override fun addRawSample(sample: PpgRawSample) {
        if (sessionId == null) {
            return
        }

        pendingSamples[sample.timestamp] = PendingSample(
            rawSample = sample,
            breathingPhase = calculateBreathingPhase(sample.timestamp)
        )
    }

    override fun addProcessedSample(sample: PpgProcessedSample): PpgSessionSample? {
        val pendingSample = pendingSamples.remove(sample.timestamp)
            ?: return null

        val sessionSample = PpgSessionSample(
            timestamp = sample.timestamp,
            green = pendingSample.rawSample.green,
            red = pendingSample.rawSample.red,
            infrared = pendingSample.rawSample.infrared,
            processedValue = sample.value,
            breathingPhase = pendingSample.breathingPhase
        )

        sessionSamples.add(sessionSample)

        return sessionSample
    }

    override fun finish(endedAt: Long): PpgMeasurementSession {
        check(pendingSamples.isEmpty()) {
            "PPG measurement session contains unprocessed samples."
        }

        val session = PpgMeasurementSession(
            id = requireNotNull(sessionId),
            startedAt = requireNotNull(startedAt),
            endedAt = endedAt,
            samples = sessionSamples.toList()
        )

        Log.d(
            "SESION",
            """
        PPG SESSION
        id=${session.id}
        startedAt=${session.startedAt}
        endedAt=${session.endedAt}
        duration=${session.endedAt - session.startedAt} ms
        samples=${session.samples.size}
        firstSamples=${session.samples.take(3)}
        lastSamples=${session.samples.takeLast(3)}
        """.trimIndent()
        )

        return session
    }

    override fun reset() {
        pendingSamples.clear()
        sessionSamples.clear()

        sessionId = null
        startedAt = null
    }

    private fun calculateBreathingPhase(
        timestamp: Long
    ): PpgBreathingPhase {
        val elapsedMillis = (timestamp - requireNotNull(startedAt)).coerceAtLeast(0L)

        val inhaleEnd = PpgConfig.BREATHING_INHALE_DURATION_MILLIS
        val inhaleHoldEnd = inhaleEnd + PpgConfig.BREATHING_INHALE_HOLD_DURATION_MILLIS
        val exhaleEnd = inhaleHoldEnd + PpgConfig.BREATHING_EXHALE_DURATION_MILLIS
        val cycleDuration = exhaleEnd + PpgConfig.BREATHING_EXHALE_HOLD_DURATION_MILLIS

        require(cycleDuration > 0L) {
            "Breathing cycle duration must be greater than zero."
        }

        val cyclePosition =
            elapsedMillis % cycleDuration

        return when {
            cyclePosition < inhaleEnd ->
                PpgBreathingPhase.INHALE

            cyclePosition < inhaleHoldEnd ->
                PpgBreathingPhase.INHALE_HOLD

            cyclePosition < exhaleEnd ->
                PpgBreathingPhase.EXHALE

            else ->
                PpgBreathingPhase.EXHALE_HOLD
        }
    }

    override fun hasPendingSamples(): Boolean {
        return pendingSamples.isNotEmpty()
    }
}