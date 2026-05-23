package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PtColorScheme = darkColorScheme(
    primary = RedBrand,
    onPrimary = TextLight,
    secondary = CarbonSlate,
    onSecondary = TextLight,
    tertiary = MacroProteinGreen,
    onTertiary = TextLight,
    background = MatteBlack,
    onBackground = TextLight,
    surface = CarbonSlate,
    onSurface = TextLight,
    surfaceVariant = SteelGray,
    onSurfaceVariant = TextMuted,
    error = Color.Red,
    onError = Color.White
)

@Composable
fun PtTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = PtColorScheme,
        typography = Typography,
        content = content
    )
}
