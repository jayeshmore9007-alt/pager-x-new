package com.example.pagerx.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PagerColors = darkColorScheme(
    primary = Color(0xFF00FFC2),
    secondary = Color(0xFF36A9FF),
    background = Color(0xFF030908),
    surface = Color(0xFF071B18),
    error = Color(0xFFFF4264)
)

@Composable
fun PagerXTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PagerColors, content = content)
}
