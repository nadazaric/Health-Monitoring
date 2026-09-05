package com.healthmonitoring.mobile.feature.ppg.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.healthmonitoring.mobile.feature.ppg.consts.PpgConfig.GRAPH_WINDOW_MILLIS
import com.healthmonitoring.mobile.feature.ppg.domain.model.PpgSessionSample
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import com.healthmonitoring.mobile.ui.theme.Dimens

@Composable
fun PpgGraph(
    samples: List<PpgSessionSample>,
    modifier: Modifier = Modifier
) {
    if (samples.size < 2) {
        return
    }

    val lineColor = MaterialTheme.colorScheme.primary

    val firstTimestamp = samples.first().timestamp
    val lastTimestamp = samples.last().timestamp
    val totalDurationMillis = lastTimestamp - firstTimestamp
    val maxWindowStartMillis =
        (totalDurationMillis - GRAPH_WINDOW_MILLIS).coerceAtLeast(0L)

    var windowStartMillis by remember(samples) {
        mutableFloatStateOf(0f)
    }

    val windowStartTimestamp =
        firstTimestamp + windowStartMillis.toLong()

    val windowEndMillis =
        (windowStartMillis + GRAPH_WINDOW_MILLIS)
            .coerceAtMost(totalDurationMillis.toFloat())

    val windowEndTimestamp =
        firstTimestamp + windowEndMillis.toLong()

    val visibleSamples = samples.filter { sample ->
        sample.timestamp in windowStartTimestamp..windowEndTimestamp
    }

    Column(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(samples) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()

                        if (maxWindowStartMillis == 0L || size.width == 0) {
                            return@detectHorizontalDragGestures
                        }

                        val draggedMillis =
                            -dragAmount / size.width * GRAPH_WINDOW_MILLIS

                        windowStartMillis = (
                                windowStartMillis + draggedMillis
                                ).coerceIn(
                                0f,
                                maxWindowStartMillis.toFloat()
                            )
                    }
                }
        ) {
            if (visibleSamples.size < 2) {
                return@Canvas
            }

            val minimumValue = visibleSamples.minOf { it.processedValue }
            val maximumValue = visibleSamples.maxOf { it.processedValue }
            val valueRange = maximumValue - minimumValue

            val path = Path()

            visibleSamples.forEachIndexed { index, sample ->
                val x =
                    ((sample.timestamp - windowStartTimestamp).toFloat() /
                            GRAPH_WINDOW_MILLIS) * size.width

                val normalizedValue = if (valueRange == 0.0) {
                    0.5f
                } else {
                    ((sample.processedValue - minimumValue) / valueRange).toFloat()
                }

                val y = size.height - normalizedValue * size.height

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = Dimens.PpgGraphStrokeWidth)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SmallSpacing),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatGraphTime(windowStartMillis.toLong()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = formatGraphTime(windowEndMillis.toLong()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatGraphTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1_000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return "%d:%02d".format(minutes, seconds)
}