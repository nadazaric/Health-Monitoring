package com.healthmonitoring.mobile.feature.skin_temperature.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.healthmonitoring.mobile.R
import com.healthmonitoring.mobile.core.presentation.HealthMetricCard
import com.healthmonitoring.mobile.feature.skin_temperature.presentation.SkinTemperatureState
import com.healthmonitoring.mobile.feature.skin_temperature.presentation.SkinTemperatureViewModel

@Composable
fun SkinTemperatureCard(
    modifier: Modifier = Modifier,
    viewModel: SkinTemperatureViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    HealthMetricCard(
        iconId = R.drawable.ic_temperature,
        iconDescriptionId = R.string.skin_temperature_description,
        modifier = modifier
    ) {
        Column {
            Text(
                text = stringResource(R.string.skin_temperature_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = getSkinTemperatureText(state),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun getSkinTemperatureText(
    state: SkinTemperatureState
): String {
    return state.objectTemperature?.let { temperature ->
        stringResource(
            R.string.skin_temperature_value,
            temperature
        )
    } ?: stringResource(R.string.no_data)
}