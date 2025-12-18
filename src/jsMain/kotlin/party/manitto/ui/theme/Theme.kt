package party.manitto.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 컬러 팔레트
val Purple500 = Color(0xFF667eea)
val Purple700 = Color(0xFF5a6fd6)
val Purple200 = Color(0xFFBB86FC)

val Green500 = Color(0xFF28a745)
val Red500 = Color(0xFFdc3545)
val Yellow500 = Color(0xFFffc107)

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    onPrimary = Color.White,
    primaryContainer = Purple200,
    secondary = Purple700,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    error = Red500,
)

@Composable
fun ManittoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}

