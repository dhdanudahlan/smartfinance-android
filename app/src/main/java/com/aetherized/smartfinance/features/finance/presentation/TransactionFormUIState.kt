package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.Transaction

sealed class TransactionFormUIState {
    data object  Empty : TransactionFormUIState()
    data class Success (
        val transaction: Transaction?,
        val category: Category?,
        val allCategories: List<Category>
    ) : TransactionFormUIState()
    data class Error(val message: String) : TransactionFormUIState()
}
