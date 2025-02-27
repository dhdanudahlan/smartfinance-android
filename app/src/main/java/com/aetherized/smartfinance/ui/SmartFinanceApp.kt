package com.aetherized.smartfinance.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.aetherized.smartfinance.core.navigation.graph.RootNavGraph

@Composable
fun SmartFinanceApp(
    appState: SmartFinanceAppState = rememberSmartFinanceAppState()
) {
    val navController = rememberNavController()

    RootNavGraph(
        navController = navController,
        startDestination = Screen.Home
    )
}

