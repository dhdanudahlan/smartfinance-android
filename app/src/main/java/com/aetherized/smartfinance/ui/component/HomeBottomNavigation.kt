package com.aetherized.smartfinance.ui.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.aetherized.smartfinance.ui.Screen

@Composable
fun HomeBottomNavigation(
    screens: List<Screen>,
    onNavigateTo: (Screen) -> Unit,
    currentDestination: NavDestination?
) {

    Log.d("navigation", "HomeBottomNavigation")

    AnimatedVisibility(
        visible = true,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {

        SmartFinanceNavigationBar {
            screens.forEach { screen ->
                Log.d("navigation", "HomeBottomNavigation: hierarchy = $currentDestination")

                val selected: Boolean =
                    currentDestination?.hierarchy?.any { it.route == screen.route } ?: false

                SmartFinanceNavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateTo(screen) },
                    icon = {
                        Icon(
                            imageVector = screen.unselectedIcon ?: Icons.Default.Warning,
                            contentDescription = screen.route
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = screen.selectedIcon ?: Icons.Default.Warning,
                            contentDescription = screen.route
                        )
                    },
                    label = { Text(text = stringResource(screen.titleTextId))}
                )
            }
        }
    }
}
