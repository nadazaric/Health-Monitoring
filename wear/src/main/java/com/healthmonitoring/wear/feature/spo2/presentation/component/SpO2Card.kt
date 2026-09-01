package com.healthmonitoring.wear.feature.spo2.presentation.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.core.presentation.component.HealthMetricCard
import com.healthmonitoring.wear.feature.spo2.presentation.SpO2State
import com.healthmonitoring.wear.feature.spo2.presentation.SpO2ViewModel
import com.healthmonitoring.wear.ui.theme.Motion

@Composable
fun SpO2Card(
    modifier: Modifier = Modifier,
    viewModel: SpO2ViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val textAlpha = getTextAlpha(state)

    HealthMetricCard(
        iconId = R.drawable.ic_spo2,
        iconDescriptionId = R.string.spo2_description,
        modifier = modifier,
        enabled = !state.isMeasuring,
        onClick = viewModel::startMeasurement
    ) {
        Column {
            Text(
                modifier = Modifier.alpha(textAlpha),
                text = getSpO2Text(state),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.alpha(textAlpha),
                text = getSpO2SubtitleText(state),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun getTextAlpha(
    state: SpO2State
): Float {
    if (!state.isMeasuring) {
        return 1f
    }

    val measuringTransition = rememberInfiniteTransition(
        label = "SpO2 measuring fade"
    )

    val measuringAlpha by measuringTransition.animateFloat(
        initialValue = Motion.MeasuringFadeInitialAlpha,
        targetValue = Motion.MeasuringFadeTargetAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Motion.MeasuringFadeDurationMs
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SpO2 measuring alpha"
    )

    return measuringAlpha
}

@Composable
private fun getSpO2Text(
    state: SpO2State
): String {
    return when {
        state.isMeasuring -> stringResource(R.string.spo2_measuring)

        state.spo2 != null -> stringResource(
            R.string.spo2_value,
            state.spo2
        )

        else -> stringResource(R.string.no_data)
    }
}

@Composable
private fun getSpO2SubtitleText(
    state: SpO2State
): String {
    return when {
        state.errorMessage != null -> {
            stringResource(R.string.spo2_measurement_failed)
        }

        state.isMeasuring -> {
            stringResource(R.string.spo2_keep_still)
        }

        else -> {
            stringResource(R.string.spo2_tap_to_measure)
        }
    }
}