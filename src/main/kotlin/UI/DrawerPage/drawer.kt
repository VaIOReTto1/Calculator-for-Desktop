package UI.DrawerPage

import UI.CalculatorPage.CalculatorPage
import UI.ExchangeRate.ExchangeRatePage
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

object Page {
    var page = mutableStateOf(0)
}

@Composable
fun HomePage() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                DrawerPage()
            }
        }
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.surface)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
                modifier = Modifier.padding(start = 7.dp, top = 1.dp)
            ) {
                Icon(Icons.Rounded.Menu, "menu")
            }

            when (Page.page.value) {
                0 -> CalculatorPage()
                1 -> ExchangeRatePage()
                else -> Text("Page not found")
            }
        }
    }
}


