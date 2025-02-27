package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import java.time.YearMonth

sealed class TransactionsUIState {
    data object Loading : TransactionsUIState()
    data class Success(
        val transactions: List<Transaction>,
        val categories: List<Category>,
        val selectedCategory: Category?,
        val selectedCategoryType: CategoryType,
        val yearMonth: YearMonth,
        val dailySummaries: List<DailySummary>
    ) : TransactionsUIState()
    data class Error(val message: String) : TransactionsUIState()
}
