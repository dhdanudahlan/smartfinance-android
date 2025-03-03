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
                    defaultValue = 0L // Use 0L to denote "no transaction" (create mode)
                }
            )
        ) { backStackEntry ->
            // Retrieve the transactionId argument.
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            // If transactionId equals 0L, treat it as null (create mode).
            TransactionFormScreenContainer(
                transactionId = if (transactionId == 0L) null else transactionId,
                onTransactionSaved = { }
            )
        }
    }
}
