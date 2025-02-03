package com.aetherized.smartfinance.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aetherized.smartfinance.core.navigation.ext.navigateTo
import com.aetherized.smartfinance.core.navigation.screen.homeScreen
import com.aetherized.smartfinance.ui.Screen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Screen
) {
    NavHost(
        navController = navController,
        route = "root_host",
        startDestination = startDestination.route,
        modifier = modifier
    ) {

        val navigateBack: () -> Unit = {
            navController.navigateUp()
        }
        homeScreen(onNavigateToRoot = navController::navigateTo)
    }
}
