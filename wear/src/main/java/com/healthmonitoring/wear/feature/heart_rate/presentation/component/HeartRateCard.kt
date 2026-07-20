package com.healthmonitoring.wear.feature.heart_rate.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.core.presentation.component.HealthMetricCard
import com.healthmonitoring.wear.feature.heart_rate.presentation.HeartRateState
import com.healthmonitoring.wear.feature.heart_rate.presentation.HeartRateViewModel

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
        Text(
            text = getHeartRateText(state),
            style = MaterialTheme.typography.titleLarge
        )
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