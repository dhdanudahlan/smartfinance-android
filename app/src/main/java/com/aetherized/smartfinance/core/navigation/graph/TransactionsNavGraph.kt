package com.aetherized.smartfinance.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aetherized.smartfinance.features.finance.presentation.screen.TransactionFormScreenContainer
import com.aetherized.smartfinance.features.finance.presentation.screen.TransactionsScreenContainer
import com.aetherized.smartfinance.ui.Screen

@Composable
fun TransactionsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onNavigateToRoot: (Screen) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Transactions.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Transactions.route
        ) {
            TransactionsScreenContainer(
                onNavigate = { route -> navController.navigate(route) },
            )
        }
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
