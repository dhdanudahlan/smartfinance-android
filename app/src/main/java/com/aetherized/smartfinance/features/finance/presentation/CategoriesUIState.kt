package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType

sealed class CategoriesUIState {
    object Loading : CategoriesUIState()

    data class Success(
        val categories: List<Category>,
        val selectedType: CategoryType? = null
    ) : CategoriesUIState()

    data class Error(val message: String) : CategoriesUIState()
}
