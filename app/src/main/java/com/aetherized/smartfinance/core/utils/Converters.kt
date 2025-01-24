package com.aetherized.smartfinance.core.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCategoryType(value: CategoryType): String = value.name

    @TypeConverter
    fun toCategoryType(value: String): CategoryType = CategoryType.valueOf(value)
}
