package UI

import Config.CALCULATOR_PADDING
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//输入数字展示
@Composable
fun Display(
    modifier: Modifier,
    mainOutput: MutableState<TextFieldValue>
) {
    val displayTextStyle = TextStyle(
        fontSize = 48.sp
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(CALCULATOR_PADDING)
            .background(MaterialTheme.colors.surface)
            .border(color = Color.Gray, width = 1.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = mainOutput.value.text,
            style = displayTextStyle,
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            maxLines = 1,
        )
    }
}