package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.MonthlySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import java.time.YearMonth

data class FilterState(
    val selectedTab: TransactionsTab = TransactionsTab.Daily,
    val yearMonth: YearMonth = YearMonth.now()
)

sealed class TransactionsTab(val displayName: String) {
    object Daily : TransactionsTab("Daily")
    object Monthly : TransactionsTab("Monthly")
    object Total : TransactionsTab("Total")
}


sealed class TransactionsUiState {
    abstract val filterState: FilterState
    abstract val categories: List<Category>
    data class Loading(
        override val filterState: FilterState = FilterState(),
        override val categories: List<Category> = emptyList()
    ) : TransactionsUiState()
    data class Success(
        val transactions: List<Transaction> = emptyList(),
        val dailySummaries: List<DailySummary> = emptyList(),
        val monthlySummaries: List<MonthlySummary> = emptyList(),
        override val filterState: FilterState = FilterState(),
        override val categories: List<Category> = emptyList()
    ) : TransactionsUiState()
    data class Error(
        val message: String,
        override val filterState: FilterState = FilterState(),
        override val categories: List<Category> = emptyList()
    ) : TransactionsUiState()
}
