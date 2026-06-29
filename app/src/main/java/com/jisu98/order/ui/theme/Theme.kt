package com.jisu98.order.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TossBlue,
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = TossGray900,
    secondary = TossGray700,
    onSecondary = Color.White,
    background = TossGray200,
    onBackground = TossGray900,
    surface = TossGray200,
    onSurface = TossGray900,
    surfaceVariant = TossGray300,
    onSurfaceVariant = TossGray600,
    error = TossRed,
    outline = TossGray300,
)

@Composable
fun OrderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}
