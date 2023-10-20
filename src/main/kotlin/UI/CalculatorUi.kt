package UI

import Config.CALCULATOR_PADDING
import UI.CalculatorStyle.KEY_ACTIVE_BACKGROUND
import UI.CalculatorStyle.KEY_BORDER_COLOR
import UI.CalculatorStyle.KEY_BORDER_WIDTH
import UI.KeyboardLayout.HigherITFKeyboardLayout
import UI.KeyboardLayout.HigherKeyboardLayout
import UI.KeyboardLayout.SimpleKeyboardLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object CalculatorStyle {
    val KEY_BORDER_WIDTH = 1.dp
    val KEY_BORDER_COLOR = Color(0xfffafafa)
    val KEY_ACTIVE_BACKGROUND = Color(0xffd4d4d4)
}

//计算器背景
@Composable
fun CalculatorKeyBackground(modifier: Modifier) = Box(
    modifier = modifier.fillMaxWidth()
        .background(MaterialTheme.colors.background)
        .border(width = KEY_BORDER_WIDTH, color = KEY_BORDER_COLOR)
)

//数字或功能键设计
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CalculatorKey(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    children: @Composable ColumnScope.() -> Unit
) {
    //判断是否点击或者处在此键位置
    var active by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(CALCULATOR_PADDING).fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = { onClick() },
                indication = null, // 设置为 null 去掉水波纹效果
                interactionSource = remember { MutableInteractionSource() }
            )
            .background(if (active) KEY_ACTIVE_BACKGROUND else MaterialTheme.colors.background)
            .onPointerEvent(PointerEventType.Move) {
            }
            .onPointerEvent(PointerEventType.Enter) {
                active = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                active = false
            } //.then(if (active) Modifier.width(70.dp) else Modifier.fillMaxWidth())
        ,
        content = children
    )
}

//数字或功能键文字展示
@Composable
fun CalculatorKeyView(modifier: Modifier, key: Key?, mainOutput: MutableState<TextFieldValue>) {
    if (key == null) {
        return CalculatorKeyBackground(modifier)
    }

    CalculatorKey(
        modifier = modifier.padding(1.dp),
        onClick = key.onClick?.let { { it(mainOutput) } } ?: {
            val textValue = mainOutput.value.text.let {
                if (it == "0") key.value else it + key.value
            }
            mainOutput.value = TextFieldValue(text = textValue)
        }
    ) {
        if (key.icon == null) {
            //无图功能键
            val textStyle = if (key.type == KeyType.Command) {
                TextStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    shadow = Shadow(
                        color = Color(0xffffe8cc),
                        offset = Offset(2.toFloat(), 2.toFloat()),
                        blurRadius = 2.toFloat()
                    )
                )
            } else {
                if (key.sHtype == KeyType.Simple) {
                    TextStyle(
                        color = Color(0xff121212),
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Monospace,
                        shadow = Shadow(
                            color = Color(0xffd0d0d0),
                            offset = Offset(2.toFloat(), 2.toFloat()),
                            blurRadius = 2.toFloat()
                        )
                    )
                } else {
                    TextStyle(
                        color = Color(0xff888888),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        shadow = Shadow(
                            color = Color(0xffdbdbdb),
                            offset = Offset(2.toFloat(), 2.toFloat()),
                            blurRadius = 2.toFloat()
                        )
                    )
                }
            }//数字键
            Text(
                text = key.value,
                style = textStyle
            )
        } else {
            Icon(
                contentDescription = "",
                imageVector = key.icon,
                tint = MaterialTheme.colors.primary
            )
        }//带图功能键
    }
}

@Composable
fun CalculatorKeyboard(mainOutput: MutableState<TextFieldValue>) {
    val currentKeyBoard = if (KeyBoardValues) {
        if (!IFTValues) HigherKeyboardLayout else HigherITFKeyboardLayout
    } else SimpleKeyboardLayout
    Row(modifier = Modifier.fillMaxSize()) {
        currentKeyBoard.forEach { keyColumn ->
            Column(modifier = Modifier.weight(1f)) {
                keyColumn.forEach { key ->
                    CalculatorKeyView(Modifier.weight(1f), key, mainOutput)
                }
            }
        }
    }
}

@Composable
fun Calculator(
    modifier: Modifier,
    mainOutput: MutableState<TextFieldValue>
) {
    Surface(modifier) {
        CalculatorKeyboard(mainOutput)
    }
}