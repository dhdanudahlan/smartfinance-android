package com.aetherized.smartfinance.core.datastore

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            // Log the error details
            Log.e("UserPreferencesSerializer", "Failed to parse UserPreferences proto: ${exception.message}", exception)
            // Optionally, you can add additional actions here (like notifying a crash analytics system)
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        try {
            t.writeTo(output)
        } catch (exception: Exception) {
            Log.e("UserPreferencesSerializer", "Error writing proto: ${exception.message}", exception)
            throw exception
        }
    }
}
