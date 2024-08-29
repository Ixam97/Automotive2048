package com.ixam97.automotive2048.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val iconButtonSize = 65.dp

private val defaultColorScheme = darkColorScheme(
    background = Color.Black,
    surface = clubNightVariant,
    onBackground = Color.White,
    onSurface = Color.White,
    primary = clubBlue,
    onPrimary = Color.White,
    surfaceContainer = clubNight

)

private val polestarColorScheme = defaultColorScheme.copy(
    primary = polestarOrange,
    surface = polestarSurface,
    surfaceContainer = polestarDarkSurface
)

@Composable
fun Automotive2048Theme(
    uniqueTheme: String = "",
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = when(uniqueTheme) {
            "Polestar" -> polestarColorScheme
            else -> defaultColorScheme
        },
        typography = when(uniqueTheme) {
                "Polestar" -> defaultPolestarTypography
                else -> Typography
        },
        content = content
    )
}