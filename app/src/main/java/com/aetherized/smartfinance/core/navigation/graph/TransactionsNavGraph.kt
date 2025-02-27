package com.aetherized.smartfinance.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aetherized.smartfinance.features.finance.presentation.screen.TransactionDetailsScreenReady
import com.aetherized.smartfinance.ui.Screen

@Composable
fun TransactionsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TransactionInfo.route,
        modifier = modifier
    ) {
        composable(Screen.TransactionInfo.route) {

        }

        composable(
            route = "transaction_details/{transactionId}"
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")?.toLong() ?: -1

            TransactionDetailsScreenReady(
                onNavigateBack = {}
            )
        }
    }
}
