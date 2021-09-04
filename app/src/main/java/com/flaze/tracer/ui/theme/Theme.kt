package com.flaze.tracer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = cyan400,
    primaryVariant = black,
    secondary = teal200
)

private val LightColorPalette = lightColors(
    primary = cyan800,
    primaryVariant = cyan900,
    secondary = teal200,

    //Other default colors to override
    background = white,
    surface = white,
    onPrimary = white,
    onSecondary = black,
    onBackground = black,
    onSurface = black,
)


@Composable
fun FlazeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = colors.primaryVariant
        )

        // setStatusBarsColor() and setNavigationBarsColor() also exist
    }


    MaterialTheme(
        colors = colors,
        typography =Typography,
        shapes = Shapes,
        content = content
    )

}