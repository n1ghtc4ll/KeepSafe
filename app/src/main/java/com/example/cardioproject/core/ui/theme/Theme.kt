package com.example.cardioproject.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val ColorScheme.tabataContainer: Color
    @Composable get() = TabataPurpleBg

val ColorScheme.inputBackground: Color
    @Composable get() = GrayInput

// 2. Стандартные роли Material
private val KeepSafeLightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    background = WhiteBg,
    surface = WhiteBg,
    surfaceVariant = GraySurface,
    onSurface = TextDark,
    onSurfaceVariant = TextGray,
    outlineVariant = DividerGray,
    error = AlertRed
)

// 3. Обертка темы
@Composable
fun KeepSafeProjectTheme(content: @Composable () -> Unit) {
    val spacing = Spacing()

    CompositionLocalProvider(
        LocalSpacing provides spacing
    ) {
        MaterialTheme(
            colorScheme = KeepSafeLightColorScheme,
            typography = Typography,
            content = content
        )
    }
}