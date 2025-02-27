package com.aetherized.smartfinance.features.home.presentation

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aetherized.smartfinance.ui.component.HomeBottomNavigation
import com.aetherized.smartfinance.ui.Screen
import com.aetherized.smartfinance.ui.component.ScreenBackground

@Composable
fun HomeScreen(
    nestedNavGraph: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = bottomBar,
        contentWindowInsets = WindowInsets(left = 0, top = 0, bottom = 0, right = 0)
    ) { paddingValues ->
        ScreenBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            nestedNavGraph.invoke()
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {

    val bottomBar: @Composable () -> Unit = {
        Log.d("navigationPreview", "homeNavGraph:bottomBar")
        HomeBottomNavigation(
            // TODO: Update this to be changeable
            screens = listOf(
                Screen.Accounts,
                Screen.Transactions,
                Screen.Reports,
                Screen.Others
            ),
            onNavigateTo = { },
            currentDestination = null
        )
    }
    HomeScreen(
        nestedNavGraph = {},
        bottomBar = bottomBar
    )
}
