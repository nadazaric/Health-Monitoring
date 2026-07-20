package com.healthmonitoring.wear.feature.skin_temperature.presentation.component

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.healthmonitoring.wear.R
import com.healthmonitoring.wear.feature.skin_temperature.presentation.SkinTemperatureState
import com.healthmonitoring.wear.feature.skin_temperature.presentation.SkinTemperatureViewModel
import com.healthmonitoring.wear.ui.theme.Dimens

@Composable
fun SkinTemperatureCard(
    modifier: Modifier = Modifier,
    viewModel: SkinTemperatureViewModel = hiltViewModel()
) {
    SkinTemperatureCardContent(
        state = viewModel.state.value,
        modifier = modifier
    )
}

@Composable
private fun SkinTemperatureCardContent(
    state: SkinTemperatureState,
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
            modifier = Modifier.size(Dimens.CardIconSize),
            painter = painterResource(id = R.drawable.ic_temperature),
            contentDescription = stringResource(R.string.skin_temperature_description),
            tint = Color.White
        )

        Text(
            text = getSkinTemperatureText(state),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun getSkinTemperatureText(
    state: SkinTemperatureState
): String {
    return state.objectTemperature?.let { temperature ->
        stringResource(
            R.string.skin_temperature_value,
            temperature
        )
    } ?: stringResource(R.string.no_data)
}