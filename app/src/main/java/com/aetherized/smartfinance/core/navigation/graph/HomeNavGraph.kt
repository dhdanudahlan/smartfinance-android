package com.aetherized.smartfinance.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aetherized.smartfinance.core.navigation.screen.accountsScreen
import com.aetherized.smartfinance.core.navigation.screen.othersScreen
import com.aetherized.smartfinance.core.navigation.screen.recordsScreen
import com.aetherized.smartfinance.core.navigation.screen.reportsScreen
import com.aetherized.smartfinance.ui.Screen

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateToRoot: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Records.route,
        modifier = modifier
    ) {
        accountsScreen()
        recordsScreen()
        reportsScreen()
        othersScreen()
    }
}
