package com.aetherized.smartfinance.core.datastore

import com.aetherized.smartfinance.core.datastore.model.DarkThemeConfig
import com.aetherized.smartfinance.core.datastore.model.UserData
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
class UserPreferencesDataSourceTest {

    // Simulate a DataStore with in-memory data.
    // You can use libraries like Turbine or FakeDataStore for testing.
    private val fakePreferences = UserPreferences.newBuilder()
        .setDarkThemeConfig(DarkThemeConfigProto.DARK_THEME_CONFIG_DARK)
        .setUseDynamicColor(true)
        .setShouldHideOnboarding(false)
        .build()

    // Assume you have a way to create a fake DataStore that always returns fakePreferences.
    // This is a simplified example.
    private val fakeDataStore = FakeDataStore(fakePreferences)

    private val dataSource = UserPreferencesDataSource(fakeDataStore)

    @Test
    fun `darkThemeConfig mapping returns correct domain enum`() = runBlockingTest {
        val userData: UserData = dataSource.userData.first()
        assertThat(userData.darkThemeConfig).isEqualTo(DarkThemeConfig.DARK)
    }
}

class FakeDataStore(private val preferences: UserPreferences) : DataStore<UserPreferences> {
    override val data: Flow<UserPreferences> = flowOf(preferences)

    override suspend fun updateData(transform: suspend (t: UserPreferences) -> UserPreferences): UserPreferences {
        // For testing, simply return the transformed value.
        return transform(preferences)
    }
}

