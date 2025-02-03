package com.aetherized.smartfinance.features.records.presentation

import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import com.aetherized.smartfinance.features.records.domain.model.Transaction
import com.aetherized.smartfinance.ui.state.UIState

sealed class RecordsUIState : UIState {
    data class Loading(val selectedCategoryType: CategoryType) : RecordsUIState()
    data class Success(
        val transactions: List<Transaction>,
        val categories: List<Category>,
        val selectedCategory: Category?,
        val selectedCategoryType: CategoryType
    ) : RecordsUIState()
    data class Error(val message: String) : RecordsUIState()
}
