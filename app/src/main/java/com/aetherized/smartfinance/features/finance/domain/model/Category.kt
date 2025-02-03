package com.aetherized.smartfinance.features.finance.domain.model

import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.features.finance.data.dto.CategoryDto
import java.time.LocalDateTime
import java.time.ZoneId
data class Category (
    val id: Long = 0L,
    val name: String,
    val type: CategoryType,
    val color: String? = null,
    val icon: String? = null,
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime = LocalDateTime.now(),
){
    fun toEntity(): CategoryEntity {
        if (id != (-1).toLong()) {
            return CategoryEntity(
                id = this.id,
                name = this.name,
                type = this.type,
                color = this.color,
                icon = this.icon,
                isDeleted = this.isDeleted,
                lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        } else {
            return CategoryEntity(
                name = this.name,
                type = this.type,
                color = this.color,
                icon = this.icon,
                isDeleted = this.isDeleted,
                lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        }
    }
    fun toDto(): CategoryDto {
        return CategoryDto(
            id = this.id,
            name = this.name,
            type = this.type,
            color = this.color,
            icon = this.icon,
            isDeleted = this.isDeleted,
            lastModified = this.lastModified.toString()
        )
    }
}

enum class CategoryType {
    EXPENSE, INCOME
}
