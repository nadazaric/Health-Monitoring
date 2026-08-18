package com.healthmonitoring.wear.ui.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme

private val WearColorScheme = ColorScheme(
    primary = Turquoise,
    primaryDim = TurquoiseDim,
    primaryContainer = TurquoiseContainer,
    onPrimary = OnTurquoise,
    onPrimaryContainer = OnTurquoiseContainer,

    secondary = LightPurple,
    secondaryDim = LightPurpleDim,
    secondaryContainer = LightPurpleContainer,
    onSecondary = OnLightPurple,
    onSecondaryContainer = OnLightPurpleContainer,

    tertiary = DeepPurple,
    tertiaryDim = DeepPurpleDim,
    tertiaryContainer = DeepPurpleContainer,
    onTertiary = OnDeepPurple,
    onTertiaryContainer = OnDeepPurpleContainer,

    background = AppBackground,
    onBackground = Cream,

    surfaceContainerLow = SurfaceLow,
    surfaceContainer = Surface,
    surfaceContainerHigh = SurfaceHigh,
    onSurface = Cream,
    onSurfaceVariant = SecondaryText,

    outline = Outline,
    outlineVariant = OutlineVariant,

    error = Error,
    errorContainer = ErrorContainer,
    onError = OnError,
    onErrorContainer = OnErrorContainer
)

@Composable
fun HealthMonitoringTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WearColorScheme,
        content = content
    )
}