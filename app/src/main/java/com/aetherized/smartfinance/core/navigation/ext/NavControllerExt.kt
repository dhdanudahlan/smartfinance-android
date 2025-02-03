package com.aetherized.smartfinance.core.navigation.ext

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.aetherized.smartfinance.ui.Screen

fun NavController.navigateTo(
    screen: Screen
) {
    val currentRoute: String? = this.currentBackStackEntry?.destination?.route

    val route = screen.routePath?.let { routePath ->
        screen.route.replaceAfter("/", routePath)
    } ?: screen.route

    Log.d("navigation", "navigateTo: ${screen.route}")

    navigate(route) {
        Log.d("navigation", "findStartDestination: ${graph.findStartDestination()}")

        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

        launchSingleTop = true

        restoreState = screen.restoreState

        if (screen.clearBackStack && !currentRoute.isNullOrEmpty()) {
            popUpTo(currentRoute) {
                inclusive = true
            }
        }
    }
}
