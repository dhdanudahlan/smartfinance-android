package com.aetherized.smartfinance.features.transactions

import com.aetherized.smartfinance.core.model.data.Category
import com.aetherized.smartfinance.core.model.data.DailySummary
import com.aetherized.smartfinance.core.model.data.MonthlySummary
import com.aetherized.smartfinance.core.model.data.Transaction
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
