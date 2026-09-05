package com.healthmonitoring.mobile.feature.spo2.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.healthmonitoring.mobile.R
import com.healthmonitoring.mobile.core.presentation.HealthMetricCard
import com.healthmonitoring.mobile.feature.spo2.presentation.SpO2State
import com.healthmonitoring.mobile.feature.spo2.presentation.SpO2ViewModel

@Composable
fun SpO2Card(
    modifier: Modifier = Modifier,
    viewModel: SpO2ViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    HealthMetricCard(
        iconId = R.drawable.ic_spo2,
        iconDescriptionId = R.string.spo2_description,
        modifier = modifier
    ) {
        Column {
            Text(
                text = stringResource(R.string.spo2_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = getSpO2Text(state),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun getSpO2Text(
    state: SpO2State
): String {
    return state.spo2?.let { spO2 ->
        stringResource(
            R.string.spo2_value,
            spO2
        )
    } ?: stringResource(R.string.no_data)
}