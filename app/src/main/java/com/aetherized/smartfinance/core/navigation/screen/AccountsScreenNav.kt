package com.aetherized.smartfinance.core.navigation.screen

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aetherized.smartfinance.ui.Screen

fun NavGraphBuilder.accountsScreen() {
    composable(
        route = Screen.Accounts.route
    ) {

        Log.d("navigation", "------accountNavGraph:START------------")
//
//        val navController = rememberNavController()
//
//        val nestedNavGraph: @Composable () -> Unit = {
//            RecordsNavGraph(
//                navController = navController
//            )
//        }



        Log.d("navigation", "------accountNavGraph:END------------")
    }
}
