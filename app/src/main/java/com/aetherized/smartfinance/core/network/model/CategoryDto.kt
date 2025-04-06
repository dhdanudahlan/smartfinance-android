package com.aetherized.smartfinance.core.network.model

import com.aetherized.smartfinance.core.model.data.CategoryType
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
