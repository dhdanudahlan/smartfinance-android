package com.aetherized.smartfinance.core.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aetherized.smartfinance.core.navigation.ext.navigateTo
import com.aetherized.smartfinance.core.navigation.screen.homeScreenNav
import com.aetherized.smartfinance.ui.navigationRouteHome

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        route = "root_host",
        startDestination = navigationRouteHome,
        modifier = modifier
    ) {

        val navigateBack: () -> Unit = {
            navController.navigateUp()
        }
        homeScreenNav(onNavigateToRoot = navController::navigateTo)
    }
}
