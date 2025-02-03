package com.aetherized.smartfinance.core.datastore.model


/**
 * Class summarizing user interest data
 */
data class UserData(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
)
