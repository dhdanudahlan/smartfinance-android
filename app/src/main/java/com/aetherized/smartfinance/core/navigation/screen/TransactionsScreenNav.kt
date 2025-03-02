package com.aetherized.smartfinance.core.navigation.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aetherized.smartfinance.core.navigation.graph.TransactionsNavGraph
import com.aetherized.smartfinance.ui.Screen

fun NavGraphBuilder.transactionsScreen(onNavigateToRoot: (Screen) -> Unit) {
    composable(
        route = Screen.Transactions.route
    ) {

        Log.d("navigation", "------transactionNavGraph:START------------")

        // NavController for nested graph
        // It will not work for root graph
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

//        val bottomBar: @Composable () -> Unit = {
//            Log.d("navigation", "homeNavGraph:bottomBar")
//            HomeBottomNavigation(
//                // TODO: Update this to be changeable
//                screens = listOf(
//                    Screen.Accounts,
//                    Screen.Transactions,
//                    Screen.Reports,
//                    Screen.Others
//                ),
//                onNavigateTo = navController::navigateTo,
//                currentDestination = navBackStackEntry?.destination
//            )
//        }

//        val nestedNavGraph: @Composable () -> Unit = {
//            Log.d("navigation", "homeNavGraph:nestedNavGraph")
//            TransactionsNavGraph(
//                navController = navController,
//                onNavigateToRoot = onNavigateToRoot
//            )
//        }

        TransactionsNavGraph(
            navController = navController,
            onNavigateToRoot = onNavigateToRoot
        )


        Log.d("navigation", "------transactionNavGraph:END------------")
    }

}
