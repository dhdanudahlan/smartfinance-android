package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction

sealed class RecordsUIState {
    data class Loading(val selectedCategoryType: CategoryType) : RecordsUIState()
    data class Success(
        val transactions: List<Transaction>,
        val categories: List<Category>,
        val selectedCategory: Category?,
        val selectedCategoryType: CategoryType
    ) : RecordsUIState()
    data class Error(val message: String) : RecordsUIState()
}
