package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.wear.compose.material3.MaterialTheme
import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.enumeration.PpgBreathingPhase
import com.healthmonitoring.wear.ui.theme.Dimens

@Composable
fun PpgBreathingProgressRing(
    phase: PpgBreathingPhase,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(phase) {
        when (phase) {
            PpgBreathingPhase.INHALE -> {
                progress.snapTo(0f)
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = PpgConfig.BREATHING_INHALE_DURATION_MILLIS.toInt(),
                        easing = LinearEasing
                    )
                )
            }

            PpgBreathingPhase.INHALE_HOLD -> {
                progress.snapTo(1f)
            }

            PpgBreathingPhase.EXHALE -> {
                progress.snapTo(1f)
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = PpgConfig.BREATHING_EXHALE_DURATION_MILLIS.toInt(),
                        easing = LinearEasing
                    )
                )
            }

            PpgBreathingPhase.EXHALE_HOLD -> {
                progress.snapTo(0f)
            }
        }
    }

    val progressColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)

    Canvas(
        modifier = modifier
    ) {
        val strokeWidth = Dimens.BreathingRingStrokeWidth.toPx()
        val inset = strokeWidth / 2f + Dimens.BreathingRingInset.toPx()

        val diameter = size.minDimension - inset * 2
        val left = (size.width - diameter) / 2f
        val top = (size.height - diameter) / 2f

        val ringTopLeft = Offset(
            x = left,
            y = top
        )

        val ringSize = Size(
            width = diameter,
            height = diameter
        )

        drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = ringTopLeft,
            size = ringSize,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = 360f * progress.value,
            useCenter = false,
            topLeft = ringTopLeft,
            size = ringSize,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}