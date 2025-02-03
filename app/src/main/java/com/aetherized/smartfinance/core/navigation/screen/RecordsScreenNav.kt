package com.aetherized.smartfinance.core.navigation.screen

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aetherized.smartfinance.features.records.presentation.screen.RecordsScreen
import com.aetherized.smartfinance.ui.Screen

fun NavGraphBuilder.recordsScreen() {
    composable(
        route = Screen.Records.route
    ) {

        Log.d("navigation", "------transactionNavGraph:START------------")
//
//        val navController = rememberNavController()
//
//        val nestedNavGraph: @Composable () -> Unit = {
//            RecordsNavGraph(
//                navController = navController
//            )
//        }

        RecordsScreen()


        Log.d("navigation", "------transactionNavGraph:END------------")
    }
}
