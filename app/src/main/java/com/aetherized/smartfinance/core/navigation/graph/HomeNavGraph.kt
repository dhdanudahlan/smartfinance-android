package com.aetherized.smartfinance.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aetherized.smartfinance.core.navigation.screen.accountsScreen
import com.aetherized.smartfinance.core.navigation.screen.othersScreen
import com.aetherized.smartfinance.core.navigation.screen.reportsScreen
import com.aetherized.smartfinance.core.navigation.screen.transactionsScreen
import com.aetherized.smartfinance.ui.Screen

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateToRoot: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Transactions.route,
        modifier = modifier
    ) {
        accountsScreen()
        transactionsScreen(onNavigateToRoot)
        reportsScreen()
        othersScreen()
    }
}
