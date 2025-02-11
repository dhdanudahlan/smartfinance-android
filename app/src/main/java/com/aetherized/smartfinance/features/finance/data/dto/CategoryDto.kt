package com.aetherized.smartfinance.features.finance.data.dto

import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import java.time.LocalDateTime

data class CategoryDto (
    val id: Long = 0L,
    val name: String,
    val type: CategoryType,
    val color: String? = null,
    val icon: String? = null,
    val isDeleted: Boolean = false,
    val lastModified: String = LocalDateTime.now().toString(),
)
