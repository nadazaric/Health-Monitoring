package com.healthmonitoring.mobile.feature.heart_rate.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.healthmonitoring.mobile.R
import com.healthmonitoring.mobile.core.presentation.HealthMetricCard
import com.healthmonitoring.mobile.feature.heart_rate.presentation.HeartRateState
import com.healthmonitoring.mobile.feature.heart_rate.presentation.HeartRateViewModel

@Composable
fun HeartRateCard(
    modifier: Modifier = Modifier,
    viewModel: HeartRateViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    HealthMetricCard(
        iconId = R.drawable.ic_heart,
        iconDescriptionId = R.string.heart_rate_description,
        modifier = modifier
    ) {
        Column {
            Text(
                text = stringResource(R.string.heart_rate_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = getHeartRateText(state),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun getHeartRateText(
    state: HeartRateState
): String {
    return state.bpm?.let { bpm ->
        stringResource(
            R.string.heart_rate_value,
            bpm
        )
    } ?: stringResource(R.string.no_data)
}