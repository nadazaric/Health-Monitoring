package com.healthmonitoring.wear.core.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import com.healthmonitoring.wear.ui.theme.Dimens

@Composable
fun HealthMetricCard(
    @DrawableRes iconId: Int,
    @StringRes iconDescriptionId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            enabled = enabled,
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(
                    Dimens.CardCornerRadius
                )
            )
            .then(clickableModifier)
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
            painter = painterResource(id = iconId),
            contentDescription = stringResource(iconDescriptionId),
            tint = Color.White
        )

        content()
    }
}