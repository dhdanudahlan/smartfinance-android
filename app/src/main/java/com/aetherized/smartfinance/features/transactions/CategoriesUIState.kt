package com.aetherized.smartfinance.features.transactions

import com.aetherized.smartfinance.core.model.data.Category
import com.aetherized.smartfinance.core.model.data.CategoryType

sealed class CategoriesUIState {
    object Loading : CategoriesUIState()

    data class Success(
        val categories: List<Category>,
        val selectedType: CategoryType? = null
    ) : CategoriesUIState()

    data class Error(val message: String) : CategoriesUIState()
}
