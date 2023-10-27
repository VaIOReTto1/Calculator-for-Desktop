import Config.lightThemeColors
import UI.DrawerPage.HomePage
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val isOpen = rememberSaveable { mutableStateOf(true) }
    if (isOpen.value) {
        Window(
            onCloseRequest = {
                isOpen.value = false
            },
            title = "Calculator",
            state = rememberWindowState(width = 380.dp, height = 620.dp),
        ) {
            MaterialTheme(colors = lightThemeColors) {
                HomePage()
            }
        }
    }
}