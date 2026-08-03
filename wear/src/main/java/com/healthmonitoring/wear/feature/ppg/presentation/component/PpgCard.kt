package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.core.presentation.component.HealthMetricCard
import com.healthmonitoring.wear.feature.ppg.presentation.PpgState
import com.healthmonitoring.wear.feature.ppg.presentation.PpgViewModel

@Composable
fun PpgCard(
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    HealthMetricCard(
        iconId = R.drawable.ic_heart,
        iconDescriptionId = R.string.ppg_description,
        modifier = modifier,
        onClick = {
            if (state.isMeasuring) {
                viewModel.stopMeasurement()
            } else {
                viewModel.startMeasurement()
            }
        }
    ) {
        Column {
            Text(
                text = getPpgText(state),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = getPpgStatusText(state),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = getPpgActionText(state),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun getPpgText(
    state: PpgState
): String {
    val measurement = state.measurement

    return when {
        measurement != null -> {
            stringResource(
                R.string.ppg_values,
                measurement.green,
                measurement.red,
                measurement.infrared
            )
        }

        state.isMeasuring -> {
            stringResource(R.string.ppg_measuring)
        }

        else -> {
            stringResource(R.string.no_data)
        }
    }
}

@Composable
private fun getPpgStatusText(
    state: PpgState
): String {
    val measurement = state.measurement

    return when {
        state.errorMessage != null -> {
            stringResource(R.string.ppg_measurement_failed)
        }

        measurement != null -> {
            stringResource(
                R.string.ppg_statuses,
                measurement.greenStatus,
                measurement.redStatus,
                measurement.infraredStatus
            )
        }

        else -> {
            stringResource(R.string.ppg_tap_to_measure)
        }
    }
}

@Composable
private fun getPpgActionText(
    state: PpgState
): String {
    return if (state.isMeasuring) {
        stringResource(R.string.ppg_tap_to_stop)
    } else {
        stringResource(R.string.ppg_tap_to_measure)
    }
}