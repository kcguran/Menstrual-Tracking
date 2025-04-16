// presentation/theme/Theme.kt
package com.kcguran.menstrualtracking.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Kadınlara yönelik pastel tonlar
private val LightPink = Color(0xFFF8BBD0)
private val Pink = Color(0xFFF48FB1)
private val DarkPink = Color(0xFFEC407A)
private val LightPurple = Color(0xFFE1BEE7)
private val Purple = Color(0xFFCE93D8)
private val LavenderBackground = Color(0xFFF3E5F5)
private val SoftPeach = Color(0xFFFFE0B2)
private val MintGreen = Color(0xFFDCEDC8)

// Kadınlara özel tema renkleri
private val LightColorScheme = lightColorScheme(
    primary = DarkPink,
    onPrimary = Color.White,
    primaryContainer = Pink,
    onPrimaryContainer = Color.White,
    secondary = Purple,
    onSecondary = Color.White,
    secondaryContainer = LightPurple,
    onSecondaryContainer = Color.Black,
    tertiary = SoftPeach,
    background = LavenderBackground,
    surface = Color.White,
    surfaceVariant = LightPink,
    error = Color(0xFFB00020)
)

private val DarkColorScheme = darkColorScheme(
    primary = Pink,
    onPrimary = Color.Black,
    primaryContainer = DarkPink,
    onPrimaryContainer = Color.White,
    secondary = LightPurple,
    onSecondary = Color.Black,
    secondaryContainer = Purple,
    onSecondaryContainer = Color.White,
    tertiary = SoftPeach,
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF3F3F3F),
    error = Color(0xFFCF6679)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun MenstrualTrackingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}