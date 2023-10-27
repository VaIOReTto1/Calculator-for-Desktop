package UI.CalculatorPage

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Modifier


@Composable
fun CalculatorPage() {
    //初始值设为0
    val mainOutput = remember { mutableStateOf(TextFieldValue("0")) }
    Column {
        //展示计算器
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