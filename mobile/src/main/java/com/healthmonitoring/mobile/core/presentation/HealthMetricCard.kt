package com.healthmonitoring.mobile.core.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.healthmonitoring.mobile.ui.theme.Dimens

@Composable
fun HealthMetricCard(
    @DrawableRes iconId: Int,
    @StringRes iconDescriptionId: Int,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Black,
    content: @Composable ColumnScope.() -> Unit
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimens.CardHorizontalContentPadding,
                    vertical = Dimens.CardVerticalContentPadding
                ),
            verticalArrangement = Arrangement.spacedBy(
                Dimens.CardContentSpacing
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(Dimens.CardIconSize),
                painter = painterResource(id = iconId),
                contentDescription = stringResource(iconDescriptionId),
                tint = iconTint
            )

            content()
        }
    }
}