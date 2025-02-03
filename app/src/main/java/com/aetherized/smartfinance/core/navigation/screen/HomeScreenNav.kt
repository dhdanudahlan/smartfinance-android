package com.aetherized.smartfinance.core.navigation.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aetherized.smartfinance.core.navigation.HomeBottomNavigation
import com.aetherized.smartfinance.core.navigation.ext.navigateTo
import com.aetherized.smartfinance.core.navigation.graph.HomeNavGraph
import com.aetherized.smartfinance.features.home.presentation.HomeScreen
import com.aetherized.smartfinance.ui.Screen

fun NavGraphBuilder.homeScreen(onNavigateToRoot: (Screen) -> Unit) {
    composable(
        route = Screen.Home.route
    ) {
        Log.d("navigation", "------homeNavGraph:START------------")

        // NavController for nested graph
        // It will not work for root graph
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val bottomBar: @Composable () -> Unit = {
            Log.d("navigation", "homeNavGraph:bottomBar")
            HomeBottomNavigation(
                // TODO: Update this to be changeable
                screens = listOf(
                    Screen.Accounts,
                    Screen.Records,
                    Screen.Reports,
                    Screen.Others
                ),
                onNavigateTo = navController::navigateTo,
                currentDestination = navBackStackEntry?.destination
            )
        }

        val nestedNavGraph: @Composable () -> Unit = {
            Log.d("navigation", "homeNavGraph:nestedNavGraph")
            HomeNavGraph(
                navController = navController,
                onNavigateToRoot = onNavigateToRoot
            )
        }

        HomeScreen(
            bottomBar = bottomBar,
            nestedNavGraph = nestedNavGraph
        )

        Log.d("navigation", "------homeNavGraph:END------------")
    }
}
