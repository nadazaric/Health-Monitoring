package com.healthmonitoring.mobile.feature.spo2.presentation.component

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.healthmonitoring.mobile.R
import com.healthmonitoring.mobile.feature.spo2.presentation.SpO2State
import com.healthmonitoring.mobile.feature.spo2.presentation.SpO2ViewModel
import com.healthmonitoring.mobile.ui.theme.Dimens

@Composable
fun SpO2Card(
    modifier: Modifier = Modifier,
    viewModel: SpO2ViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

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
                painter = painterResource(id = R.drawable.ic_oxygen),
                contentDescription = stringResource(R.string.spo2_description),
                tint = Color.Black
            )

            Text(
                text = getSpO2Text(state),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun getSpO2Text(
    state: SpO2State
): String {
    return state.spo2?.let { spO2 ->
        stringResource(
            R.string.spo2_value,
            spO2
        )
    } ?: stringResource(R.string.no_data)
}