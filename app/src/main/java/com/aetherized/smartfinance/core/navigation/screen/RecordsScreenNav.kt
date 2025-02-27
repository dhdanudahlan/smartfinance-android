package com.aetherized.smartfinance.core.navigation.screen

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aetherized.smartfinance.features.finance.presentation.screen.TransactionsScreenReady
import com.aetherized.smartfinance.ui.Screen

fun NavGraphBuilder.transactionsScreen() {
    composable(
        route = Screen.Transactions.route
    ) {

        Log.d("navigation", "------transactionNavGraph:START------------")
//
//        val navController = rememberNavController()
//
//        val nestedNavGraph: @Composable () -> Unit = {
//            TransactionsNavGraph(
//                navController = navController
//            )
//        }

        TransactionsScreenReady()


        Log.d("navigation", "------transactionNavGraph:END------------")
    }

}
