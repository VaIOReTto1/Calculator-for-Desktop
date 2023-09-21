package UI

import CalculateService.calculate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue

data class Key(
    val value: String,
    val type: KeyType,
    val icon: ImageVector? = null,
    val onClick: ((mainOutput: MutableState<TextFieldValue>) -> Unit)? = null
)

enum class KeyType {
    Number, Command
}

fun String.number() = Key(this, type = KeyType.Number)
fun String.command() = Key(this, type = KeyType.Command)

fun createKey(
    value: String,
    type: KeyType,
    icon: ImageVector? = null,
    onClick: ((mainOutput: MutableState<TextFieldValue>) -> Unit)? = null
) =
    Key(value, type, icon, onClick)

//存储历史记录
var historyList by mutableStateOf(mutableListOf<String>())

//等于
val keyEquals = createKey("=", KeyType.Command) { mainOutput ->
    val input = mainOutput.value.text
    calculate("$input+0")?.let { result ->
        val Count = "$input=$result"
        // 将历史记录添加到链表中
        historyList.add(Count)
        mainOutput.value = TextFieldValue(text = Count)
    }
}

//清空
val keyDelete = createKey("", KeyType.Command, Icons.Rounded.KeyboardArrowLeft) { mainOutput ->
    val textValue = mainOutput.value.text
    if (textValue.isNotEmpty()) {
        mainOutput.value = TextFieldValue(
            text = if (textValue.length == 1) "0" else textValue.substring(0, textValue.length - 1)
        )
    }
}

//清除
val keyClear = createKey("AC", KeyType.Command) { mainOutput ->
    mainOutput.value = TextFieldValue("0")
}

//键盘切换
var KeyBoardValues by mutableStateOf(false)

val KeyChange = createKey("切换", KeyType.Command) {
    KeyBoardValues = !KeyBoardValues
}

//倒数
val KeyReciprocal = createKey("1/x", KeyType.Number) { mainOutput ->
    val currentText = mainOutput.value.text
    val newText = "$currentText^(-1)"
    mainOutput.value = TextFieldValue(text = newText)
}

//阶乘
val KeyFactorial = createKey("x!", KeyType.Number) { mainOutput ->
    val currentText = mainOutput.value.text
    val newText = "$currentText!"
    mainOutput.value = TextFieldValue(text = newText)
}

//反三角函数
var IFTValues by mutableStateOf(false)

val KeyITF = createKey("2nd", KeyType.Command) {
    IFTValues = !IFTValues
}

//键盘位置分布
object KeyboardLayout {
    val HigherKeyboardLayout = listOf(
        listOf(KeyITF, "√".number(), "C".number(), KeyFactorial, KeyReciprocal, "π".number(), "e".number()),
        listOf("log".number(), "^".number(), keyClear, "7".number(), "4".number(), "1".number(), KeyChange),
        listOf("sin".number(), ",".number(), keyDelete, "8".number(), "5".number(), "2".number(), "0".number()),
        listOf("cos".number(), "(".number(), "%".command(), "9".number(), "6".number(), "3".number(), ".".number()),
        listOf("tan".number(), ")".number(), "÷".command(), "x".command(), "-".command(), "+".command(), keyEquals)
    )

    val HigherITFKeyboardLayout = listOf(
        listOf(KeyITF, "√".number(), "C".number(), KeyFactorial, KeyReciprocal, "π".number(), "e".number()),
        listOf("log".number(), "^".number(), keyClear, "7".number(), "4".number(), "1".number(), KeyChange),
        listOf("arcsin".number(), ",".number(), keyDelete, "8".number(), "5".number(), "2".number(), "0".number()),
        listOf("arccos".number(), "(".number(), "%".command(), "9".number(), "6".number(), "3".number(), ".".number()),
        listOf("arctan".number(), ")".number(), "÷".command(), "x".command(), "-".command(), "+".command(), keyEquals)
    )

    val SimpleKeyboardLayout = listOf(
        listOf(keyClear, "7".number(), "4".number(), "1".number(), KeyChange),
        listOf(keyDelete, "8".number(), "5".number(), "2".number(), "0".number()),
        listOf("%".command(), "9".number(), "6".number(), "3".number(), ".".number()),
        listOf("÷".command(), "x".command(), "-".command(), "+".command(), keyEquals)
    )
}

