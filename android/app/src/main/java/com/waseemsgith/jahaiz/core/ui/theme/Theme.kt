package com.waseemsgith.jahaiz.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColors =
    darkColorScheme(
        primary = JahaizGold,
        onPrimary = JahaizBlack,
        secondary = JahaizGoldDark,
        tertiary = JahaizAccent,
        background = JahaizBlack,
        surface = JahaizDarkCard,
        onBackground = JahaizGold,
        onSurface = JahaizGold,
        error = JahaizRed,
    )

@Composable
fun JahaizTheme(content: @Composable () -> Unit) {
    val dark = true
    val sys = rememberSystemUiController()
    sys.setSystemBarsColor(JahaizBlack, darkIcons = false)

    MaterialTheme(
        colorScheme = DarkColors,
        typography = JahaizTypography,
        content = content,
    )
}
