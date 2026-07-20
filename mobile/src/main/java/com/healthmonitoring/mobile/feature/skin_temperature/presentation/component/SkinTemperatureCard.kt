package com.healthmonitoring.mobile.feature.skin_temperature.presentation.component

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
        Text(
            text = getSkinTemperatureText(state),
            style = MaterialTheme.typography.titleLarge
        )
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