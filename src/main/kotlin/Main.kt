import Config.lightThemeColors
import UI.Display
import UI.Calculator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
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
            state = rememberWindowState(width = 500.dp, height = 650.dp),
        ) {
            MaterialTheme(colors = lightThemeColors) {
                //初始值设为0
                val mainOutput = remember { mutableStateOf(TextFieldValue("0")) }
                //展示计算器
                Column(Modifier.fillMaxHeight()) {
                    Display(
                        Modifier.weight(1f),
                        mainOutput
                    )
                    Calculator(
                        Modifier.weight(4f),
                        mainOutput
                    )
                }
            }
        }
    }
}