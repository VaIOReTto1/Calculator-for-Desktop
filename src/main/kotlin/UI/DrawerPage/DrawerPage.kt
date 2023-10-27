package UI.DrawerPage

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier


@Composable
fun DrawerPage() {
    val buttonList = listOf("标准", "货币")
    var selectedTabIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        DrawerButton(buttonList, selectedTabIndex = selectedTabIndex) {
            selectedTabIndex = if (selectedTabIndex == it) -1 else it
        }
    }
}