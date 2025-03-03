package com.aetherized.smartfinance.core.datastore

import androidx.datastore.core.DataStore
import com.aetherized.smartfinance.core.datastore.model.DarkThemeConfig
import com.aetherized.smartfinance.core.datastore.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data
        .map { preferences ->
            UserData(
                darkThemeConfig = preferences.darkThemeConfig?.toDomain() ?: DarkThemeConfig.FOLLOW_SYSTEM,
                useDynamicColor = preferences.useDynamicColor,
                shouldHideOnboarding = preferences.shouldHideOnboarding
            )
        }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.useDynamicColor = useDynamicColor
            }
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData {
            it.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
    }


    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy { this.shouldHideOnboarding = shouldHideOnboarding }
        }
    }
}
