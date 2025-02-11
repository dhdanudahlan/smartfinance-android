package com.aetherized.smartfinance.features.finance.domain.model

import java.time.LocalDateTime

data class Category (
    val id: Long = 0L,
    val name: String,
    val type: CategoryType,
    val color: String? = null,
    val icon: String? = null,
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime = LocalDateTime.now(),
)

enum class CategoryType {
    EXPENSE, INCOME
}
