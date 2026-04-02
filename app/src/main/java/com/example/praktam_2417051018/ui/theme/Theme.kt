package com.example.praktam_2417051018.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = PrimaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onPrimary = TextPrimary
)

@Composable
fun PraktiktamTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}