package com.aetherized.smartfinance.core.model.data

import java.time.LocalDateTime

data class Category (
    val id: Long = 0L,
    val name: String,
    val type: CategoryType,
    val color: String? = null,
    val icon: String? = null,
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime = LocalDateTime.now(),
    val isNew: Boolean = false
)

enum class CategoryType {
    EXPENSE, INCOME
}
