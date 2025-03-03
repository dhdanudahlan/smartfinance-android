package com.aetherized.smartfinance.core.datastore

import android.content.Context
import androidx.datastore.core.DataMigration

class UserPreferencesMigration(private val context: Context) : DataMigration<UserPreferences> {

    override suspend fun shouldMigrate(currentData: UserPreferences): Boolean {
        // Example: If you add a new field, check if it’s missing or has a default value
        // Return true if migration is required.
        return false
    }

    override suspend fun migrate(currentData: UserPreferences): UserPreferences {
        // Example: If a new field 'newField' is added, set its default value.
        return currentData.toBuilder()
            // .setNewField(DEFAULT_VALUE)  // Uncomment and adjust if needed.
            .build()
    }

    override suspend fun cleanUp() {
        // Clean up any temporary resources if needed.
    }

}
