package com.aetherized.smartfinance.core.utils

import androidx.room.TypeConverter
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    // Convert LocalDateTime to Long and vice versa
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): Long =
        dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    @TypeConverter
    fun toLocalDateTime(timestamp: Long): LocalDateTime =
        Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()

    // Convert CategoryType to String and vice versa
    @TypeConverter
    fun fromCategoryType(type: CategoryType): String = type.name

    @TypeConverter
    fun toCategoryType(type: String): CategoryType = CategoryType.valueOf(type)

    // Convert SyncStatus to String and vice versa
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name

    @TypeConverter
    fun toSyncStatus(status: String): SyncStatus = SyncStatus.valueOf(status)
}
