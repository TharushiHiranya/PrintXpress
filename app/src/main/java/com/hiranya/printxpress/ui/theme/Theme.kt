package com.hiranya.printxpress.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Light-only colour scheme mapped to the brand palette.
private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = OnAccent,
    primaryContainer = AccentContainer,
    background = Background,
    surface = Background,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun PrintXpressTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}