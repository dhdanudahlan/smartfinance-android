package com.aetherized.smartfinance.features.categories.data.dto

import com.aetherized.smartfinance.core.utils.CategoryType
import com.aetherized.smartfinance.features.categories.domain.model.Category
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class CategoryDto (
    val id: Long = 0L,
    val name: String,
    val type: CategoryType,
    val color: String? = null,
    val icon: String? = null,
    val isDeleted: Boolean = false,
    val lastModified: String,
){
    fun toDomainModel(): Category {
        return Category(
            id = this.id,
            name = this.name,
            type = this.type,
            color = this.color,
            icon = this.icon,
            isDeleted = this.isDeleted,
            lastModified = LocalDateTime.parse(this.lastModified)
        )
    }
}
