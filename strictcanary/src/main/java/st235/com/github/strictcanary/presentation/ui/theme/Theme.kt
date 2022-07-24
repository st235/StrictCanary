package st235.com.github.strictcanary.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Grey50,
    primaryVariant = Grey50Dark,
    onPrimary = Grey900,
    secondary = AmberA700,
    secondaryVariant = AmberA700Dark,
    onSecondary = Color.White,
    background = Grey100,
    onBackground = Grey900,
    surface = Grey200,
    onSurface = Grey900
)

@Composable
fun StrictCanaryTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}