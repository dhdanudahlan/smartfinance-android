package com.aetherized.smartfinance.features.category

import com.aetherized.smartfinance.core.model.data.Category
import com.aetherized.smartfinance.core.model.data.CategoryType

sealed class CategoryListUiState {
    abstract val categoryType: CategoryType
    data class Loading(
        override val categoryType: CategoryType = CategoryType.EXPENSE
    ) : CategoryListUiState()
    data class Success(
        override val categoryType: CategoryType = CategoryType.EXPENSE,
        val categories: List<Category>
    ) : CategoryListUiState()
    data class Error(
        override val categoryType: CategoryType = CategoryType.EXPENSE,
        val message: String
    ) : CategoryListUiState()
}
