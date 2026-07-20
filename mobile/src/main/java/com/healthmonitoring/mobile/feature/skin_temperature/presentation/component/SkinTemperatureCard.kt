package com.healthmonitoring.mobile.feature.skin_temperature.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthmonitoring.mobile.R
import com.healthmonitoring.mobile.feature.skin_temperature.presentation.SkinTemperatureState
import com.healthmonitoring.mobile.feature.skin_temperature.presentation.SkinTemperatureViewModel
import com.healthmonitoring.mobile.ui.theme.Dimens

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
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            Dimens.CardCornerRadius
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.CardElevation
        )
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimens.CardHorizontalContentPadding,
                    vertical = Dimens.CardVerticalContentPadding
                ),
            verticalArrangement = Arrangement.spacedBy(Dimens.CardContentSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(Dimens.CardIconSize),
                painter = painterResource(id = R.drawable.ic_temperature),
                contentDescription = stringResource(
                    R.string.skin_temperature_description
                ),
                tint = Color.Black
            )

            Text(
                text = getSkinTemperatureText(state),
                style = MaterialTheme.typography.titleLarge
            )
        }
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
    } ?: stringResource(R.string.skin_temperature_unavailable)
}