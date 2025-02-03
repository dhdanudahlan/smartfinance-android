package com.aetherized.smartfinance.core.datastore

import androidx.datastore.core.DataStore
import com.aetherized.smartfinance.core.datastore.model.DarkThemeConfig
import com.aetherized.smartfinance.core.datastore.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferencesDataSource
) : UserDataRepository {
    override val userData : Flow<UserData> =
        userPreferences.userData

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.setDarkThemeConfig(darkThemeConfig)
    }


    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.setShouldHideOnboarding(shouldHideOnboarding)
    }
}
