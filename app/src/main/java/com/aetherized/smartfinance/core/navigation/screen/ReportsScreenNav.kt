package com.aetherized.smartfinance.core.navigation.screen

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aetherized.smartfinance.ui.Screen

fun NavGraphBuilder.reportsScreen() {
    composable(
        route = Screen.Reports.route
    ) {

        Log.d("navigation", "------reportsNavGraph:START------------")
//
//        val navController = rememberNavController()
//
//        val nestedNavGraph: @Composable () -> Unit = {
//            RecordsNavGraph(
//                navController = navController
//            )
//        }



        Log.d("navigation", "------reportsNavGraph:END------------")
    }
}
