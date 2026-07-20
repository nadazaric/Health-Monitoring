package com.healthmonitoring.wear.feature.spo2.presentation.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.feature.spo2.presentation.SpO2State
import com.healthmonitoring.wear.feature.spo2.presentation.SpO2ViewModel
import com.healthmonitoring.wear.ui.theme.Dimens

@Composable
fun SpO2Card(
    modifier: Modifier = Modifier,
    viewModel: SpO2ViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val textAlpha = getTextAlpha(state)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(
                    Dimens.CardCornerRadius
                )
            )
            .clickable(
                enabled = !state.isMeasuring,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = viewModel::startMeasurement
            )
            .padding(
                horizontal = Dimens.CardHorizontalContentPadding,
                vertical = Dimens.CardVerticalContentPadding
            ),
        verticalArrangement = Arrangement.spacedBy(
            Dimens.CardContentSpacing
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                Dimens.CardContentSpacing
            )
        ) {
            Icon(
                modifier = Modifier.size(Dimens.CardIconSize),
                painter = painterResource(id = R.drawable.ic_oxygen),
                contentDescription = stringResource(R.string.spo2_description),
                tint = Color.White
            )

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
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
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