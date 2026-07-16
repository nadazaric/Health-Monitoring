package com.master.healthmonitoring.feature.heartrate.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.master.healthmonitoring.R
import com.master.healthmonitoring.feature.heartrate.presentation.HeartRateState
import com.master.healthmonitoring.feature.heartrate.presentation.HeartRateViewModel
import com.master.healthmonitoring.ui.theme.Dimens

@Composable
fun HeartRateCard(
    modifier: Modifier = Modifier,
    viewModel: HeartRateViewModel = hiltViewModel()
) {
    HeartRateCardContent(
        state = viewModel.state.value,
        modifier = modifier
    )
}

@Composable
private fun HeartRateCardContent(
    state: HeartRateState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(
                    Dimens.CardCornerRadius
                )
            )
            .padding(
                horizontal = Dimens.CardHorizontalContentPadding,
                vertical = Dimens.CardVerticalContentPadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            Dimens.CardContentSpacing
        )
    ) {
        Icon(
            modifier = Modifier
                .size(Dimens.CardIconSize),
            painter = painterResource(id = R.drawable.ic_heart),
            contentDescription = stringResource(R.string.heart_rate_description),
            tint = Color.White
        )

        Text(
            text = getHeartRateText(state),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun getHeartRateText(state: HeartRateState): String {
    return state.bpm?.let { bpm ->
        stringResource(
            R.string.heart_rate_value,
            bpm
        )
    } ?: stringResource(R.string.heart_rate_unavailable)
}