package com.aetherized.smartfinance.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aetherized.smartfinance.core.utils.isSystemInDarkTheme
import com.aetherized.smartfinance.ui.MainUiState.Loading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


        // We keep this as a mutable state, so that we can track changes inside the composition.
        // This allows us to react to dark/light mode changes.
        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                disableDynamicTheming = Loading.shouldDisableDynamicTheming,
            ),
        )

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                combine(
//                    isSystemInDarkTheme(),
//                    viewModel.uiState,
//                ) { systemDark, uiState ->
//                    ThemeSettings(
//                        darkTheme = uiState.shouldUseDarkTheme(systemDark),
//                        disableDynamicTheming = uiState.shouldDisableDynamicTheming
//                    )
//                }
//                    .onEach { themeSettings = it }
//                    .map { it.darkTheme }
//                    .distinctUntilChanged()
//                    .collect{ darkTheme ->
//                        trace("smartFinanceEdgetoEdge") {
//                            enableEdgeToEdge(
//                                statusBarStyle = SystemBarStyle.auto(
//                                    lightScrim = android.graphics.Color.TRANSPARENT,
//                                    darkScrim = android.graphics.Color.TRANSPARENT,
//                                ) { darkTheme },
//                                navigationBarStyle = SystemBarStyle.auto(
//                                    lightScrim = lightScrim,
//                                    darkScrim = darkScrim,
//                                ) { darkTheme },
//                            )
//                        }
//                    }
//
//                // Keep the splash screen on-screen until the UI state is loaded. This condition is
//                // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
//                // the UI.
//                splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

                setContent {
                    SmartFinanceApp()
                }
            }
        }
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

/**
 * Class for the system theme settings.
 * This wrapping class allows us to combine all the changes and prevent unnecessary recompositions.
 */
data class ThemeSettings(
    val darkTheme: Boolean,
    val disableDynamicTheming: Boolean,
)


