package com.healthmonitoring.mobile.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.healthmonitoring.mobile.feature.ppg.presentation.PpgViewModel
import com.healthmonitoring.mobile.ui.theme.Dimens
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PpgGraphCard(
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val session = state.selectedSession ?: return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.CardElevation
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = Dimens.CardHorizontalContentPadding,
                vertical = Dimens.CardVerticalContentPadding
            )
        ) {
            Text(
                text = "PPG Signal",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = getMeasurementInfo(
                    startedAt = session.startedAt,
                    endedAt = session.endedAt
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            PpgGraph(
                samples = session.samples,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.PpgGraphHeight)
                    .padding(top = Dimens.CardContentSpacing * 2)
            )
        }
    }
}

private fun getMeasurementInfo(
    startedAt: Long,
    endedAt: Long
): String {
    val dateTime = Instant.ofEpochMilli(startedAt)
        .atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofPattern(
                "dd MMM yyyy · HH:mm",
                Locale.ENGLISH
            )
        )

    val durationSeconds = (endedAt - startedAt) / 1_000

    return "$dateTime · ${durationSeconds}s"
}