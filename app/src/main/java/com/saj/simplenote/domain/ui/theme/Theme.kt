package com.saj.simplenote.domain.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MyWhite,
    secondary = MyPurple,
    tertiary = Pink80,
    background = MyWhite,
    surface = Color(0xFFFFFBFE),
    primaryContainer = MyPurple,
    onPrimary = MyDarkPurple,
    onSecondary = MyGrey,
    onTertiary = MyPurple,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    onPrimaryContainer = MyWhite,
    error = MyRed,
    onError = MyRed
)

private val LightColorScheme = lightColorScheme(
    primary = MyWhite,
    secondary = MyPurple,
    tertiary = Pink80,
    background = MyWhite,
    surface = Color(0xFFFFFBFE),
    primaryContainer = MyPurple,
    onPrimary = MyDarkPurple,
    onSecondary = MyGrey,
    onTertiary = MyPurple,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    onPrimaryContainer = MyWhite,
    error = MyRed,
    onError = MyRed
)


@Composable
fun SimpleNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}


