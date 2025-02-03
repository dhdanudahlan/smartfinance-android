package com.aetherized.smartfinance.features.records.data.dto

import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import java.time.LocalDateTime

data class CategoryDto (
    val id: Long = 0L,
    val name: String,
    val type: CategoryType,
    val color: String? = null,
    val icon: String? = null,
    val isDeleted: Boolean = false,
    val lastModified: String = LocalDateTime.now().toString(),
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
