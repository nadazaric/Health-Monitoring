package com.healthmonitoring.mobile.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.healthmonitoring.mobile.R
import com.healthmonitoring.mobile.core.presentation.HealthMetricCard
import com.healthmonitoring.mobile.feature.ppg.presentation.PpgViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PpgCard(
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val session = state.selectedSession

    HealthMetricCard(
        iconId = R.drawable.ic_ppg,
        iconDescriptionId = R.string.ppg_description,
        modifier = modifier
    ) {
        Column {
            Text(
                text = stringResource(R.string.ppg_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            Text(
                text = session?.let {
                    getPpgDate(it.startedAt)
                } ?: stringResource(R.string.no_data),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

private fun getPpgDate(timestamp: Long): String {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofPattern(
                "dd MMM",
                Locale.ENGLISH
            )
        )
}