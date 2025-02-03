package com.aetherized.smartfinance.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aetherized.smartfinance.ui.Screen

@Composable
fun RecordsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RecordInfo.route,
        modifier = modifier
    ) {
        composable(Screen.RecordInfo.route) {

        }
    }
}
