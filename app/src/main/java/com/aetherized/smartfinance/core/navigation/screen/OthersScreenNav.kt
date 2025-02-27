package com.aetherized.smartfinance.core.navigation.screen

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aetherized.smartfinance.ui.Screen

fun NavGraphBuilder.othersScreen() {
    composable(
        route = Screen.Others.route
    ) {

        Log.d("navigation", "------othersNavGraph:START------------")
//
//        val navController = rememberNavController()
//
//        val nestedNavGraph: @Composable () -> Unit = {
//            TransactionsNavGraph(
//                navController = navController
//            )
//        }



        Log.d("navigation", "------othersNavGraph:END------------")
    }
}
