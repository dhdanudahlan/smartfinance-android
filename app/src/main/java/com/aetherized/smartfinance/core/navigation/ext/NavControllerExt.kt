package com.aetherized.smartfinance.core.navigation.ext

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.aetherized.smartfinance.ui.Screen

fun NavController.navigateTo(
    screen: Screen
) {
    val currentRoute: String? = this.currentBackStackEntry?.destination?.route

    val route = screen.route

//    Log.d("navigation", "navigateTo: ${screen.route}")

    navigate(route) {
//        Log.d("navigation", "findStartDestination: ${graph.findStartDestination()}")

        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

        launchSingleTop = true

        restoreState = true

        if (!currentRoute.isNullOrEmpty()) {
            popUpTo(currentRoute) {
                inclusive = true
            }
        }
    }
}
