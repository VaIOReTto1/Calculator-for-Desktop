package UI.ExchangePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EnergyPage() {
    val exchangeRates = mapOf(
        "食物卡路里" to 0.000239,
        "热卡路里" to 0.239,
        "千焦耳" to 0.001,
        "英国热量单位" to 0.000948,
    )

    var textValue by remember { mutableStateOf("100") }

    Column(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 13.dp, top = 13.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colors.primary)
        ) {

            Row {
                Text(
                    text = "焦耳",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )

                BasicTextField(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    value = textValue,
                    onValueChange = { textValue = if (it == "") "0" else it },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.End
                    ),
                    visualTransformation = VisualTransformation.None,
                    singleLine = true
                )
            }
        }

        Box(modifier = Modifier.height(20.dp))

        ExchangeButton(exchangeRates, textValue.toDouble())
    }
}