package UI

import Config.CALCULATOR_PADDING
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//输入数字展示
@Composable
fun Display(
    modifier: Modifier,
    mainOutput: MutableState<TextFieldValue>
) {
    var historyList1 by mutableStateOf(mutableListOf<String>())
    historyList1 = historyList
    val size=historyList1.size

    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()

    //监听historyList1的变化并滚动到底部
    LaunchedEffect(size) {
        println(size)
        if (size !=0)
            listState.scrollToItem(historyList1.lastIndex)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = CALCULATOR_PADDING, end = CALCULATOR_PADDING)
            .background(MaterialTheme.colors.surface).scrollable(scrollState, orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Bottom,
        state = listState,
    ) {
        items(historyList1) { historyEntry1 ->
            Text(
                text = historyEntry1,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(end = 2.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = CALCULATOR_PADDING, end = CALCULATOR_PADDING)
            .background(MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = mainOutput.value.text,
            fontSize = 48.sp,
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            maxLines = 1,
        )
    }
}