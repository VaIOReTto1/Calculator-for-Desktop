package UI

import Config.CALCULATOR_PADDING
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import kotlinx.coroutines.*

//输入数字展示
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun Display(
    modifier: Modifier,
    mainOutput: MutableState<TextFieldValue>
) {
    var historyList1 by mutableStateOf(mutableListOf<String>())
    historyList1= historyList

    val coroutineScope = rememberCoroutineScope()

    // 添加一个观察historyList1的LaunchedEffect
    LaunchedEffect(historyList) {
        // 等待LazyColumn测量完成
        delay(100)
        coroutineScope.launch {
            // 滚动到最底部
            val listState = LazyListState()
            listState.scrollToItem(historyList.size - 1)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(CALCULATOR_PADDING)
            .background(MaterialTheme.colors.surface)
            .border(color = Color.Gray, width = 1.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Bottom,
        state = rememberLazyListState()
    ) {
        items(historyList1) { historyEntry1 ->
            Text(
                text = historyEntry1,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth().padding(end = 2.dp),
                textAlign = TextAlign.End,
                fontSize = 36.sp,
            )
        }
    }
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
            fontSize = 48.sp,
            overflow = TextOverflow.Ellipsis,
            softWrap = false,
            maxLines = 1,
        )
    }
}