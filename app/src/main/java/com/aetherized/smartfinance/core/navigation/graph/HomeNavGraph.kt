package com.aetherized.smartfinance.core.navigation.graph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.aetherized.smartfinance.core.navigation.screen.accountsScreen
import com.aetherized.smartfinance.core.navigation.screen.othersScreen
import com.aetherized.smartfinance.core.navigation.screen.reportsScreen
import com.aetherized.smartfinance.features.finance.presentation.screen.TransactionFormScreenContainer
import com.aetherized.smartfinance.features.finance.presentation.screen.TransactionsScreenContainer
import com.aetherized.smartfinance.ui.Screen
import com.aetherized.smartfinance.ui.TransactionsScreen

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

//        transactionsScreen(onNavigateToRoot, navController)
        navigation(
            startDestination = TransactionsScreen.List.route,
            route = Screen.Transactions.route
        ) {
            composable(route = TransactionsScreen.List.route) {
                TransactionsScreenContainer (
                    onTransactionClick = { transactionId ->
                        // Navigate in edit mode with a valid Long id.
                        navController.navigate(TransactionsScreen.Form.createRoute(transactionId))
                    },
                    onFabClick = {
                        // Navigate in add mode (no transactionId provided)
                        navController.navigate(TransactionsScreen.Form.createRoute())
                    }
                )
            }
            composable(
                route = TransactionsScreen.Form.route,
                arguments = listOf(
                    navArgument("transactionId") {
                        type = NavType.LongType
                        defaultValue = 0L
                    }
                )
            ) { backStackEntry ->
                // Retrieve transactionId as a Long.
                val transactionId = backStackEntry.arguments?.getLong("transactionId")
//                Log.d("HomeNavGraph", "loadTransaction: $transactionId")
                if (transactionId == 0L) {
                    Log.d("HomeNavGraph", "loadTransaction?: 0L")
                } else {
                    Log.d("HomeNavGraph", "loadTransaction?: $transactionId")
                }
                // If transactionId equals 0L, treat it as null (create mode).
                TransactionFormScreenContainer(
                    // If transactionId is 0L, treat it as null (add mode).
//                    transactionId = if (transactionId == 0L) null else transactionId,
                    navigateToPrevious = { navController.popBackStack() }
                )
            }
        }

        reportsScreen()

        othersScreen()
    }
}
