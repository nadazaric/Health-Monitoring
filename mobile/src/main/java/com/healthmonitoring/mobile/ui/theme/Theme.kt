package com.healthmonitoring.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Turquoise,
    onPrimary = OnTurquoise,
    primaryContainer = TurquoiseContainer,
    onPrimaryContainer = OnTurquoiseContainer,

    secondary = LightPurple,
    onSecondary = OnLightPurple,
    secondaryContainer = LightPurpleContainer,
    onSecondaryContainer = OnLightPurpleContainer,

    tertiary = DeepPurple,
    onTertiary = OnDeepPurple,
    tertiaryContainer = DeepPurpleContainer,
    onTertiaryContainer = OnDeepPurpleContainer,

    background = AppBackground,
    onBackground = Cream,

    surface = Surface,
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

private val LightColorScheme = lightColorScheme(
    primary = Turquoise,
    onPrimary = OnTurquoise,
    primaryContainer = LightBlue,
    onPrimaryContainer = DeepBlue,

    secondary = DeepPurple,
    onSecondary = OnDeepPurple,
    secondaryContainer = LightPurple,
    onSecondaryContainer = OnLightPurple,

    tertiary = DeepBlue,
    onTertiary = Color.White,
    tertiaryContainer = LightBlue,
    onTertiaryContainer = DeepBlue,

    background = LightAppBackground,
    onBackground = LightPrimaryText,

    surface = LightSurface,
    surfaceContainerLow = LightSurfaceLow,
    surfaceContainer = LightSurface,
    surfaceContainerHigh = LightSurfaceHigh,
    onSurface = LightPrimaryText,
    onSurfaceVariant = LightSecondaryText,

    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    error = LightError,
    errorContainer = LightErrorContainer,
    onError = LightOnError,
    onErrorContainer = LightOnErrorContainer
)

@Composable
fun HealthMonitoringTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        },
        content = content
    )
}