package UI.ExchangePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ExchangeButton(
    items: Map<String, Double>,
    value: Double,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        items.entries.forEach { (country, rate) ->
            val textColor = Color.Black

            Box(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 5.dp, top = 5.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)).background(Color(0xffd4d4d4))
            ) {
                Row {

                    Text(
                        text = country,
                        color = textColor,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "%.6f".format(value * rate),
                        color = textColor,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(8.dp).fillMaxWidth()
                    )
                }
            }
        }
    }
}
