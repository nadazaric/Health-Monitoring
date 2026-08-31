package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.wear.feature.ppg.presentation.PpgViewModel
import com.healthmonitoring.wear.ui.theme.Dimens
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun PpgMeasurementScreen(
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.startMeasurement()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopMeasurement()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimens.ScreenHorizontalPadding,
                vertical = Dimens.ScreenVerticalPadding
            )
    ) {
        when {
            state.isPreparing -> {
                PpgPreparingAnimation(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.isMeasuring -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        space = Dimens.CardSpacing,
                        alignment = Alignment.CenterVertically
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = formatRemainingTime(
                                remainingTimeMillis = state.remainingTimeMillis
                            ),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier.weight(1f),
                            text = state.heartRate?.currentBpm?.let { bpm ->
                                stringResource(
                                    R.string.ppg_heart_rate,
                                    bpm.roundToInt()
                                )
                            } ?: stringResource(R.string.ppg_heart_rate_unavailable),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    state.breathingPhase?.let { phase ->
                        Text(
                            text = when (phase) {
                                PpgBreathingPhase.INHALE -> stringResource(R.string.ppg_breathing_inhale)
                                PpgBreathingPhase.EXHALE -> stringResource(R.string.ppg_breathing_exhale)
                                PpgBreathingPhase.INHALE_HOLD, PpgBreathingPhase.EXHALE_HOLD ->
                                    stringResource(R.string.ppg_breathing_hold)
                            },
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }

                    PpgSignalChart(
                        measurements = state.chartMeasurements,
                        peaks = state.chartPeaks
                    )
                }
            }

            state.isMeasurementCompleted -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.ppg_measurement_completed),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        state.errorMessage?.let {
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = stringResource(R.string.ppg_measurement_failed),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatRemainingTime(remainingTimeMillis: Long): String {
    val remainingSeconds = ceil(remainingTimeMillis / 1_000.0).toInt()
    return "$remainingSeconds s"
}