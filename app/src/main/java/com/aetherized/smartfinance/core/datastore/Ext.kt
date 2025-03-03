package com.aetherized.smartfinance.core.datastore

import com.aetherized.smartfinance.core.datastore.model.DarkThemeConfig

fun DarkThemeConfigProto.toDomain(): DarkThemeConfig = when (this) {
    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> DarkThemeConfig.LIGHT
    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
    // Include FOLLOW_SYSTEM and fallbacks here:
    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
    DarkThemeConfigProto.UNRECOGNIZED -> DarkThemeConfig.FOLLOW_SYSTEM

}
