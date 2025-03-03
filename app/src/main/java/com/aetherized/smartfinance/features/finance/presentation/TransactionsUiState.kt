package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import java.time.YearMonth

sealed class TransactionsUiState {
    data object Loading : TransactionsUiState()
    data class Success(
        val transactions: List<Transaction>,
        val categories: List<Category>,
        val selectedCategory: Category?,
        val selectedCategoryType: CategoryType,
        val yearMonth: YearMonth,
        val dailySummaries: List<DailySummary>
    ) : TransactionsUiState()
    data class Error(val message: String) : TransactionsUiState()
}
