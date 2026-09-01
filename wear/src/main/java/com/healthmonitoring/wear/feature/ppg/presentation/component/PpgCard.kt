package com.healthmonitoring.wear.feature.ppg.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.core.presentation.component.HealthMetricCard
import com.healthmonitoring.wear.feature.ppg.presentation.PpgState
import com.healthmonitoring.wear.feature.ppg.presentation.PpgViewModel

@Composable
fun PpgCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PpgViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    HealthMetricCard(
        iconId = R.drawable.ic_ppg,
        iconDescriptionId = R.string.ppg_description,
        modifier = modifier,
        onClick = onClick
    ) {
        Column {
            Text(
                text = stringResource(R.string.ppg_description),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(R.string.ppp_tap_to_measure),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}