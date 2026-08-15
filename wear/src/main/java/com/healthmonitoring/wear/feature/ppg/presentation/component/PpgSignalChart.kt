package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.wear.compose.material3.MaterialTheme
import com.healthmonitoring.wear.feature.ppg.consts.PpgConfig
import com.healthmonitoring.wear.feature.ppg.domain.model.PpgProcessedMeasurement
import com.healthmonitoring.wear.ui.theme.Dimens
import kotlin.math.max

@Composable
fun PpgSignalChart(
    measurements: List<PpgProcessedMeasurement>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ChartHeight)
    ) {
        if (measurements.size < 2) {
            return@Canvas
        }

        val firstTimestamp = measurements.first().timestamp
        val lastTimestamp = measurements.last().timestamp
        val timeRange = max(lastTimestamp - firstTimestamp, 1L)

        val minimumValue = measurements.minOf { it.value }
        val maximumValue = measurements.maxOf { it.value }
        val valueRange = max(
            maximumValue - minimumValue,
            PpgConfig.CHART_MIN_VALUE_RANGE
        )

        val path = Path()

        measurements.forEachIndexed { index, measurement ->
            val x = (
                    (measurement.timestamp - firstTimestamp).toFloat() /
                            timeRange.toFloat()
                    ) * size.width

            val normalizedValue = (
                    measurement.value - minimumValue
                    ) / valueRange

            val y = size.height - normalizedValue.toFloat() * size.height

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = Dimens.ChartStrokeWidth.toPx())
        )
    }
}