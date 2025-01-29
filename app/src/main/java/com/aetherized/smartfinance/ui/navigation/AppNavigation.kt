package com.aetherized.smartfinance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(navController: NavHostController) {
    val navController = rememberNavController()
    val parentState = rememberParentState()
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {

        }
        composable("categories") {
        }
        composable("transactions") {
        }
    }
}
