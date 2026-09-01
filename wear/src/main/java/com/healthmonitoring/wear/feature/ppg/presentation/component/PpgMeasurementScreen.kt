package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.wear.feature.ppg.presentation.PpgViewModel
import com.healthmonitoring.wear.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PpgMeasurementScreen(
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel(),
    onMeasurementCompleted: () -> Unit
) {
    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.startMeasurement()
    }

    LaunchedEffect(state.isMeasurementCompleted) {
        if (state.isMeasurementCompleted) {
            delay(PpgConfig.MEASUREMENT_COMPLETED_DISPLAY_MILLIS.milliseconds)
            onMeasurementCompleted()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopMeasurement()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
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
                        .padding(
                            horizontal = Dimens.ScreenHorizontalPadding,
                            vertical = Dimens.ScreenVerticalPadding / 2
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.CardSpacing)
                ) {
                    state.breathingPhase?.let { phase ->
                        Text(
                            text = when (phase) {
                                PpgBreathingPhase.INHALE ->
                                    stringResource(R.string.ppg_breathing_inhale)
                                PpgBreathingPhase.INHALE_HOLD, PpgBreathingPhase.EXHALE_HOLD ->
                                    stringResource(R.string.ppg_breathing_hold)
                                PpgBreathingPhase.EXHALE -> stringResource(R.string.ppg_breathing_exhale)
                            },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens.SmallSpacing)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.SmallSpacing),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(Dimens.IconSize),
                                painter = painterResource(R.drawable.ic_heart),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = state.heartRate?.currentBpm
                                    ?.roundToInt()
                                    ?.toString() ?: "--",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = stringResource(R.string.ppg_bpm),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        PpgSignalChart(
                            measurements = state.chartMeasurements,
                            peaks = state.chartPeaks
                        )
                    }
                }

                state.breathingPhase?.let { phase ->
                    PpgBreathingProgressRing(
                        phase = phase,
                        modifier = Modifier.matchParentSize()
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
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Dimens.ScreenVerticalPadding),
                text = stringResource(R.string.ppg_measurement_failed),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}