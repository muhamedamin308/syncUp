package com.syncup.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SyncUpBlue,
    onPrimary = NeutralWhite,
    primaryContainer = SyncUpBlueLight,
    onPrimaryContainer = SyncUpBlueDark,
    background = NeutralSurface,
    onBackground = NeutralText,
    surface = NeutralCard,
    onSurface = NeutralText,
    surfaceVariant = NeutralSurface,
    onSurfaceVariant = NeutralMuted,
    outline = NeutralBorder,
)

private val DarkColorScheme = darkColorScheme(
    primary = SyncUpBlue,
    onPrimary = NeutralWhite,
    primaryContainer = DarkCard,
    onPrimaryContainer = SyncUpBlueLight,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkMuted,
    outline = DarkBorder,
)

@Composable
fun SyncUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = run {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context)
        else dynamicLightColorScheme(context)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SyncUpTypography,
        shapes = SyncUpShapes,
        content = content
    )
}