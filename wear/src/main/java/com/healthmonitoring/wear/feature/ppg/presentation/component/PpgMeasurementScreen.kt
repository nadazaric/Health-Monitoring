package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimens.ScreenHorizontalPadding,
                vertical = Dimens.ScreenVerticalPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = Dimens.CardSpacing,
            alignment = Alignment.CenterVertically
        )
    ) {
        when {
            state.isPreparing -> {
                Text(
                    text = stringResource(R.string.ppg_preparing_signal),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            state.isMeasuring -> {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        text = formatRemainingTime(
                            remainingTimeMillis = state.remainingTimeMillis
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        text = state.heartRate?.currentBpm?.let { bpm ->
                            stringResource(
                                R.string.ppg_heart_rate,
                                bpm.roundToInt()
                            )
                        } ?: stringResource(R.string.ppg_heart_rate_unavailable),
                        textAlign = TextAlign.Center
                    )

                }

                PpgSignalChart(
                    measurements = state.chartMeasurements,
                    peaks = state.chartPeaks
                )
            }

            state.isMeasurementCompleted -> {
                Text(
                    text = stringResource(R.string.ppg_measurement_completed),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        state.errorMessage?.let {
            Text(
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