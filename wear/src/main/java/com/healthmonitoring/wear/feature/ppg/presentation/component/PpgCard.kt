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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    HealthMetricCard(
        iconId = R.drawable.ic_heart,
        iconDescriptionId = R.string.ppg_description,
        modifier = modifier,
        onClick = onClick
    ) {
        Column {
            Text(
                text = stringResource(R.string.ppg_description),
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
                text = stringResource(R.string.ppg_open_measurement),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun getPpgStatusText(
    state: PpgState
): String {
    return when {
        state.errorMessage != null -> {
            stringResource(R.string.ppg_measurement_failed)
        }

        state.isMeasuring -> {
            stringResource(R.string.ppg_measuring)
        }

        state.isMeasurementCompleted -> {
            stringResource(R.string.ppg_measurement_completed)
        }

        else -> {
            stringResource(R.string.ppg_ready_to_measure)
        }
    }
}