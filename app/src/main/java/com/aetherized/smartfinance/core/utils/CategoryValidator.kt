package com.aetherized.smartfinance.core.utils

import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.database.entity.TransactionEntity

class CategoryValidator {
    companion object {
        fun validateName(category: CategoryEntity): Boolean{
            require(category.name.isNotBlank()) { "Category name cannot be blank." }
            require(category.name.length < 256) { "Category name exceeds maximum length." }
            return true
        }
    }
}
