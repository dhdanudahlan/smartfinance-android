package com.aetherized.smartfinance.core.datastore

import com.aetherized.smartfinance.core.datastore.model.DarkThemeConfig
import com.aetherized.smartfinance.core.datastore.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the desired dark theme config.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    /**
     * Sets the preferred dynamic color config.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Sets whether the user has completed the onboarding process.
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

    suspend fun eraseDatastore()
}
