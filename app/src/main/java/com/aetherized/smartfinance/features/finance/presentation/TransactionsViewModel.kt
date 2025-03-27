package com.aetherized.smartfinance.features.finance.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.MonthlySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteTransactionUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetMonthlyTransactionsUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetYearlyTransactionsUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

sealed interface TransactionsEvent {
    object NavigateToPreviousMonth : TransactionsEvent
    object NavigateToNextMonth : TransactionsEvent
    object NavigateToPreviousYear : TransactionsEvent
    object NavigateToNextYear : TransactionsEvent
    data class FetchTransactions(val limit: Int = 50, val offset: Int = 0) : TransactionsEvent
    data class ChangeTransactionsTab(val tab: TransactionsTab) : TransactionsEvent
}

sealed class SFAccount(val id: Long, val name: String) {
    data object Imagination : SFAccount(id = 0, name = "Imagination")
    data object Cash : SFAccount(id = 1, name = "MyCash")
    data object BankAccounts : SFAccount(id = 2, name = "MyBank")
}



@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getMonthlyTransactionsUseCase: GetMonthlyTransactionsUseCase,
    private val getYearlyTransactionsUseCase: GetYearlyTransactionsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val upsertTransactionUseCase: UpsertTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    // State flows for transactions
    private val _transactionsUiState = MutableStateFlow<TransactionsUiState>(
        TransactionsUiState.Loading()
    )
    val transactionsUiState: StateFlow<TransactionsUiState> = _transactionsUiState

    init {
        onEvent(TransactionsEvent.FetchTransactions(limit = 100, offset = 0))
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            TransactionsEvent.NavigateToPreviousMonth -> navigateToPreviousMonth()
            TransactionsEvent.NavigateToNextMonth -> navigateToNextMonth()
            TransactionsEvent.NavigateToPreviousYear -> navigateToPreviousYear()
            TransactionsEvent.NavigateToNextYear -> navigateToNextYear()
            is TransactionsEvent.FetchTransactions -> {
                fetchCategories()
                fetchTransactions(event.limit, event.offset)
            }
            is TransactionsEvent.ChangeTransactionsTab -> changeTransactionsTab(event.tab)
        }
    }
    private fun fetchCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .retryWhen { _, attempt ->
                    if (attempt < 5) {
                        delay(1000)
                        true
                    } else {
                        false
                    }
                }
                .catch { e ->
                    _transactionsUiState.update {
                        TransactionsUiState.Error(e.message ?: "Error loading categories")
                    }
                }
                .collect { categories ->
                    _transactionsUiState.update { currentState ->
                        when (currentState) {
                            is TransactionsUiState.Loading -> {
                                currentState.copy(categories = categories)
                            }

                            is TransactionsUiState.Success -> {
                                currentState.copy(categories = categories)
                            }

                            else -> {
                                TransactionsUiState.Loading(categories = categories)
                            }
                        }
                    }
                }
        }
    }
    private fun <T> combineTransactionAndCategories(
        transactionsFlow: Flow<List<Transaction>>,
        generateSummaries: (transactions: List<Transaction>, categories: List<Category>) -> T,
        updateState: (transactions: List<Transaction>, categories: List<Category>, summaries: T) -> TransactionsUiState
    ) {
        viewModelScope.launch {
            combine(
                transactionsFlow,
                getCategoriesUseCase()
            ) { transactions, categories ->
                val summaries = generateSummaries(transactions, categories)

                updateState(transactions, categories, summaries)
            }
                .catch { e ->
                    _transactionsUiState.value = TransactionsUiState.Error(
                        message = e.message ?: "Error loading transactions",
                    )
                }
                .collect { newUiState ->
                    _transactionsUiState.update { newUiState }
                }
        }
    }
    private fun fetchTransactions(limit: Int = 50, offset: Int = 0) {
        viewModelScope.launch {
            if (transactionsUiState.value.filterState.selectedTab == TransactionsTab.Daily) {
                val transactionsFlow = getMonthlyTransactionsUseCase(
                        yearMonth = transactionsUiState.value.filterState.yearMonth,
                        limit = limit,
                        offset = offset
                    )
                combineTransactionAndCategories(
                    transactionsFlow = transactionsFlow,
                    generateSummaries = { transactions, categories ->
                        generateDailySummaries(
                            transactions = transactions,
                            categories = categories,
                        )
                    },
                    updateState = { transactions, categories, summaries ->
                        TransactionsUiState.Success(
                            transactions = transactions,
                            dailySummaries = summaries,
                            filterState = transactionsUiState.value.filterState,
                            categories = categories
                        )
                    }
                )
            } else if (transactionsUiState.value.filterState.selectedTab == TransactionsTab.Monthly) {
                val transactionsFlow = getYearlyTransactionsUseCase(
                    yearMonth = transactionsUiState.value.filterState.yearMonth,
                    limit = limit,
                    offset = offset
                )
                combineTransactionAndCategories(
                    transactionsFlow = transactionsFlow,
                    generateSummaries = { transactions, categories ->
                        generateMonthlySummaries(
                            transactions = transactions,
                            categories = categories,
                        )
                    },
                    updateState = { transactions, categories, summaries ->
                        TransactionsUiState.Success(
                            transactions = transactions,
                            monthlySummaries = summaries,
                            filterState = transactionsUiState.value.filterState,
                            categories = categories
                        )
                    }
                )
            }
        }
    }


    private fun generateDailySummaries(
        transactions: List<Transaction> = emptyList(),
        categories: List<Category> = emptyList()
    ): List<DailySummary> {
        val incomeId = categories.filter { it.type == CategoryType.INCOME }.map { it.id }
        val expenseId = categories.filter { it.type == CategoryType.EXPENSE }.map { it.id }

        // Group transactions by date
        val transactionsByDate = transactions.groupBy { it.timestamp.toLocalDate() }

        return transactionsByDate.map { (date, transactionsForDate) ->
            val totalIncome = transactionsForDate.filter { it.categoryId in incomeId }.sumOf { it.amount }
            val totalExpense = transactionsForDate.filter { it.categoryId in expenseId }.sumOf { it.amount }

            DailySummary(date, transactionsForDate, totalIncome, totalExpense)
        }
    }
    private fun generateMonthlySummaries(
        transactions: List<Transaction> = emptyList(),
        categories: List<Category> = emptyList()
    ): List<MonthlySummary> {
        val incomeId = categories.filter { it.type == CategoryType.INCOME }.map { it.id }
        val expenseId = categories.filter { it.type == CategoryType.EXPENSE }.map { it.id }

        // Group transactions by date.
        val transactionsByMonth = transactions.groupBy { YearMonth.from(it.timestamp) }

        return transactionsByMonth.map { (month, transactionsForDate) ->
            val totalIncome = transactionsForDate.filter { it.categoryId in incomeId }.sumOf { it.amount }
            val totalExpense = transactionsForDate.filter { it.categoryId in expenseId }.sumOf { it.amount }

            MonthlySummary(month, transactionsForDate, totalIncome, totalExpense)
        }
    }

    // Navigation actions to update the month.
    private fun navigateToPreviousMonth() {
        Log.d("TransactionsViewModel", "navigateToPreviousMonth")
        _transactionsUiState.update { currentState ->
            val currentFilter = (currentState as? TransactionsUiState.Success)?.filterState ?: (currentState as? TransactionsUiState.Loading)?.filterState ?: FilterState()
            TransactionsUiState.Loading(currentFilter.copy(yearMonth = currentFilter.yearMonth.minusMonths(1)))
        }
        fetchTransactions()
    }

    private fun navigateToNextMonth() {
        Log.d("TransactionsViewModel", "navigateToNextMonth")
        _transactionsUiState.update { currentState ->
            val currentFilter = (currentState as? TransactionsUiState.Success)?.filterState ?: (currentState as? TransactionsUiState.Loading)?.filterState ?: FilterState()
            TransactionsUiState.Loading(currentFilter.copy(yearMonth = currentFilter.yearMonth.plusMonths(1)))
        }
        fetchTransactions()
    }

    // Navigation actions to update the month.
    private fun navigateToPreviousYear() {
        Log.d("TransactionsViewModel", "navigateToPreviousYear")
        _transactionsUiState.update { currentState ->
            val currentFilter = (currentState as? TransactionsUiState.Success)?.filterState ?: (currentState as? TransactionsUiState.Loading)?.filterState ?: FilterState()
            TransactionsUiState.Loading(currentFilter.copy(yearMonth = currentFilter.yearMonth.minusYears(1)))
        }
        fetchTransactions()
    }

    private fun navigateToNextYear() {
        Log.d("TransactionsViewModel", "navigateToNextYear")
        _transactionsUiState.update { currentState ->
            val currentFilter = (currentState as? TransactionsUiState.Success)?.filterState ?: (currentState as? TransactionsUiState.Loading)?.filterState ?: FilterState()
            TransactionsUiState.Loading(currentFilter.copy(yearMonth = currentFilter.yearMonth.plusYears(1)))
        }
        fetchTransactions()
    }

    private fun changeTransactionsTab(tab: TransactionsTab = TransactionsTab.Daily) {
        _transactionsUiState.update { currentState ->
            val currentFilter = (currentState as? TransactionsUiState.Success)?.filterState ?: (currentState as? TransactionsUiState.Loading)?.filterState ?: FilterState()
            TransactionsUiState.Loading(currentFilter.copy(selectedTab = tab))
        }
        fetchTransactions()

    }
}
