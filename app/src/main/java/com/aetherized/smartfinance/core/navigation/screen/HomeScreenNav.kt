package com.aetherized.smartfinance.core.navigation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aetherized.smartfinance.core.navigation.ext.navigateTo
import com.aetherized.smartfinance.core.navigation.graph.HomeNavGraph
import com.aetherized.smartfinance.features.home.screen.HomeScreen
import com.aetherized.smartfinance.features.main.ui.Screen
import com.aetherized.smartfinance.features.main.ui.TransactionsScreen
import com.aetherized.smartfinance.features.main.ui.component.HomeBottomNavigation
import com.aetherized.smartfinance.features.main.ui.navigationRouteHome

fun NavGraphBuilder.homeScreenNav(onNavigateToRoot: (Screen) -> Unit) {
    composable(
        route = navigationRouteHome
    ) {
//        Log.d("navigation", "------homeNavGraph:START------------")

        // NavController for nested graph
        // It will not work for root graph
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val displayBottomBar = when (navBackStackEntry?.destination?.route) {
            Screen.Accounts.route -> true
            Screen.Transactions.route -> true
            TransactionsScreen.List.route -> true
            Screen.Reports.route -> true
            Screen.Others.route -> true
            else -> false
        }
//        Log.d("navigation", "displayBottomBar: $displayBottomBar")
//        Log.d("navigation", "displayBottomBar Target Route: ${navBackStackEntry?.destination?.route}")
//        Log.d("navigation", "displayBottomBar Current Route: ${Screen.Transactions.route}")
        val bottomBar: @Composable () -> Unit = {
//            Log.d("navigation", "homeNavGraph:bottomBar")
            if (displayBottomBar) {
                HomeBottomNavigation(
                    // TODO: Update this to be changeable
                    screens = listOf(
                        Screen.Accounts,
                        Screen.Transactions,
                        Screen.Reports,
                        Screen.Others
                    ),
                    onNavigateTo = navController::navigateTo,
                    currentDestination = navBackStackEntry?.destination
                )
            }
        }

        val nestedNavGraph: @Composable () -> Unit = {
//            Log.d("navigation", "homeNavGraph:nestedNavGraph")
            HomeNavGraph(
                navController = navController,
                onNavigateToRoot = onNavigateToRoot
            )
        }

        HomeScreen(
            bottomBar = bottomBar,
            nestedNavGraph = nestedNavGraph
        )

//        Log.d("navigation", "------homeNavGraph:END------------")
    }
}
