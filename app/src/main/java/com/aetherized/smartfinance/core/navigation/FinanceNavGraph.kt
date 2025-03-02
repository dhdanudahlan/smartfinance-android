package com.aetherized.smartfinance.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aetherized.smartfinance.features.finance.presentation.screen.TransactionFormScreenContainer

@Composable
fun FinanceNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "transaction_form?transactionId={transactionId}"
    ) {
        // Single route for both create and edit, with an optional transactionId parameter.
        composable(
            route = "transaction_form?transactionId={transactionId}",
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                    defaultValue = -1L // Use -1 to denote "no transaction" (create mode)
                }
            )
        ) { backStackEntry ->
            // Retrieve the transactionId argument.
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: -1L
            // If transactionId equals -1, treat it as null (create mode).
            TransactionFormScreenContainer(
                transactionId = if (transactionId == -1L) null else transactionId,
                navController = navController
            )
        }
    }
}
