package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.feature.ppg.presentation.PpgViewModel
import kotlin.math.ceil

@Composable
fun PpgMeasurementScreen(
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            text = stringResource(R.string.ppg_description),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = formatRemainingTime(
                remainingTimeMillis = state.remainingTimeMillis
            ),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Text(
            text = when {
                state.isMeasuring ->
                    stringResource(R.string.ppg_measuring)

                state.isMeasurementCompleted ->
                    stringResource(
                        R.string.ppg_measurement_completed
                    )

                else ->
                    stringResource(R.string.ppg_ready_to_measure)
            },
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        state.errorMessage?.let {
            Text(
                text = stringResource(
                    R.string.ppg_measurement_failed
                ),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = {
                if (state.isMeasuring) {
                    viewModel.stopMeasurement()
                } else {
                    viewModel.startMeasurement()
                }
            }
        ) {
            Text(
                text = if (state.isMeasuring) {
                    stringResource(
                        R.string.ppg_stop_measurement
                    )
                } else {
                    stringResource(
                        R.string.ppg_start_measurement
                    )
                }
            )
        }
    }
}

private fun formatRemainingTime(
    remainingTimeMillis: Long
): String {
    val remainingSeconds = ceil(
        remainingTimeMillis / 1_000.0
    ).toInt()

    return "$remainingSeconds s"
}