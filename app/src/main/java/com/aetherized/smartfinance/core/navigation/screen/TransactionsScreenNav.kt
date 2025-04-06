package com.aetherized.smartfinance.core.navigation.screen

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.aetherized.smartfinance.features.transactions.screen.TransactionFormScreenContainer
import com.aetherized.smartfinance.features.transactions.screen.TransactionsScreenContainer
import com.aetherized.smartfinance.features.main.ui.Screen
import com.aetherized.smartfinance.features.main.ui.TransactionsScreen

fun NavGraphBuilder.transactionsScreen(
    onNavigateToRoot: (Screen) -> Unit,
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Transactions.route,
        route = Screen.Transactions.route
    ) {
        composable(
            route = Screen.Transactions.route
        ) {
            TransactionsScreenContainer(
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
            route = "transaction_form?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                    defaultValue = 0L // Use 0L to denote "no transaction" (create mode)
                }
            )
        ) { backStackEntry ->
            // Retrieve the transactionId argument.
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            // If transactionId equals 0L, treat it as null (create mode).
            TransactionFormScreenContainer(
//                transactionId = if (transactionId == 0L) null else transactionId,
                navigateToPrevious = { navController.popBackStack() }
            )
        }
    }
//    composable(
//        route = Screen.Transactions.route
//    ) {
//
//        Log.d("navigation", "------transactionNavGraph:START------------")
//
//        // NavController for nested graph
//        // It will not work for root graph
//        val navController = rememberNavController()
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//
//        TransactionsNavGraph(
//            navController = navController,
//            onNavigateToRoot = onNavigateToRoot
//        )
//
//
//        Log.d("navigation", "------transactionNavGraph:END------------")
//    }

}
