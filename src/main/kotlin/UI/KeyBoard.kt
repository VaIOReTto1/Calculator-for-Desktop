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
    val sHtype:KeyType?=null,
    val icon: ImageVector? = null,
    val onClick: ((mainOutput: MutableState<TextFieldValue>) -> Unit)? = null
)

enum class KeyType {
    Number, Command,Simple, Higher
}

fun String.Simnumber() = Key(this, type = KeyType.Number,KeyType.Simple)
fun String.Hignumber() = Key(this, type = KeyType.Number,KeyType.Higher)
fun String.command() = Key(this, type = KeyType.Command)

fun createKey(
    value: String,
    type: KeyType,
    sHtype:KeyType?=null,
    icon: ImageVector? = null,
    onClick: ((mainOutput: MutableState<TextFieldValue>) -> Unit)? = null
) =
    Key(value, type, sHtype, icon,onClick)

//存储历史记录
var historyList by mutableStateOf(mutableListOf<String>())

//等于
val keyEquals = createKey("=", KeyType.Command) { mainOutput ->
    val input = mainOutput.value.text
    calculate(input)?.let { result ->
        val Count = "$input=$result"
        // 将历史记录添加到链表中
        historyList.add(Count)
        mainOutput.value = TextFieldValue(text = Count)
    }
}

//清空
val keyDelete = createKey("", KeyType.Command, icon = Icons.Rounded.KeyboardArrowLeft) { mainOutput ->
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
        listOf(KeyITF, "√".Hignumber(), "C".Hignumber(), KeyFactorial, KeyReciprocal, "π".Hignumber(), KeyChange),
        listOf("log".Hignumber(), "^".Hignumber(), keyClear, "7".Simnumber(), "4".Simnumber(), "1".Simnumber(), "e".Simnumber()),
        listOf("sin".Hignumber(), ",".Hignumber(), keyDelete, "8".Simnumber(), "5".Simnumber(), "2".Simnumber(), "0".Simnumber()),
        listOf("cos".Hignumber(), "(".Hignumber(), "%".command(), "9".Simnumber(), "6".Simnumber(), "3".Simnumber(), ".".Simnumber()),
        listOf("tan".Hignumber(), ")".Hignumber(), "÷".command(), "x".command(), "-".command(), "+".command(), keyEquals)
    )

    val HigherITFKeyboardLayout = listOf(
        listOf(KeyITF, "√".Hignumber(), "C".Hignumber(), KeyFactorial, KeyReciprocal, "π".Hignumber(), KeyChange),
        listOf("log".Hignumber(), "^".Hignumber(), keyClear, "7".Simnumber(), "4".Simnumber(), "1".Simnumber(), "e".Simnumber()),
        listOf("arcsin".Hignumber(), ",".Hignumber(), keyDelete, "8".Simnumber(), "5".Simnumber(), "2".Simnumber(), "0".Simnumber()),
        listOf("arccos".Hignumber(), "(".Hignumber(), "%".command(), "9".Simnumber(), "6".Simnumber(), "3".Simnumber(), ".".Simnumber()),
        listOf("arctan".Hignumber(), ")".Hignumber(), "÷".command(), "x".command(), "-".command(), "+".command(), keyEquals)
    )

    val SimpleKeyboardLayout = listOf(
        listOf(keyClear, "7".Simnumber(), "4".Simnumber(), "1".Simnumber(), KeyChange),
        listOf(keyDelete, "8".Simnumber(), "5".Simnumber(), "2".Simnumber(), "0".Simnumber()),
        listOf("%".command(), "9".Simnumber(), "6".Simnumber(), "3".Simnumber(), ".".Simnumber()),
        listOf("÷".command(), "x".command(), "-".command(), "+".command(), keyEquals)
    )
}

