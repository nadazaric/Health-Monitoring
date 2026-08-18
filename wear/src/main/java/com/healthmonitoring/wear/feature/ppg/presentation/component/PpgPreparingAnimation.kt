package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.ui.theme.Dimens

@Composable
fun PpgPreparingAnimation(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(
        label = "PPG preparing animation"
    )

    val pulseScale by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1_500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PPG pulse scale"
    )

    val pulseAlpha by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1_500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PPG pulse alpha"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = modifier.size(Dimens.PreparingAnimationSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(Dimens.PreparingAnimationSize)
        ) {
            drawCircle(
                color = primaryColor.copy(alpha = pulseAlpha * 0.35f),
                radius = size.minDimension / 2f * pulseScale,
                style = Stroke(
                    width = Dimens.PreparingCircleStrokeWidth.toPx()
                )
            )

            drawCircle(
                color = secondaryColor.copy(alpha = pulseAlpha),
                radius = size.minDimension / 2.4f * pulseScale,
                style = Stroke(
                    width = Dimens.PreparingCircleStrokeWidth.toPx()
                )
            )
        }

        Column(
            modifier = Modifier.size(Dimens.PreparingCircleSize),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = Dimens.PreparingTextSpacing,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(
                text = stringResource(R.string.ppg_preparing_relax),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.ppg_preparing_stay_still),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}