package UI.DrawerPage

import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch


@Composable
fun DrawerPage(drawerState: DrawerState) {
    val buttonList = listOf("标准", "货币", "容量", "长度", "重量", "温度", "能量")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxHeight()
    ) {

        DrawerButton(buttonList, selectedTabIndex = selectedTabIndex) {
            selectedTabIndex = if (selectedTabIndex == it) -1 else it

            coroutineScope.launch {
                drawerState.close()
            }
        }
    }
}