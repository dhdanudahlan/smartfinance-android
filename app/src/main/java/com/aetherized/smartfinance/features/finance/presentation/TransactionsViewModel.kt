package com.aetherized.smartfinance.features.finance.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteTransactionUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetTransactionsUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val upsertTransactionUseCase: UpsertTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<TransactionsUiState>(
        TransactionsUiState.Loading
    )
    val uiState: StateFlow<TransactionsUiState> = _uiState

    private var selectedCategory: Category? = null
    private var selectedCategoryType: CategoryType = CategoryType.EXPENSE
    private var currentYearMonth: YearMonth = YearMonth.now()

    init {
        Log.d("UI", "TransactionsViewModel init")
        loadTransactions()
    }

    private fun loadTransactions(limit: Int = 50, offset: Int = 0) {
        _uiState.value = TransactionsUiState.Loading

        viewModelScope.launch {
            try {
                combine(
                    getTransactionsUseCase(
                        categoryId = selectedCategory?.id,
                        categoryType = if (selectedCategory == null) selectedCategoryType else null,
                        limit = limit,
                        offset = offset
                    ),
                    getCategoriesUseCase(
                        categoryType = selectedCategoryType,
                        limit = limit,
                        offset = offset
                    )
                ) { transactions, categories ->
                    val dailySummaries = computeDailySummaries(
                        transactions = transactions,
                        categories = categories,
                        yearMonth = currentYearMonth
                    )
                    TransactionsUiState.Success(
                        transactions = transactions,
                        categories = categories,
                        yearMonth = currentYearMonth,
                        selectedCategory = selectedCategory,
                        selectedCategoryType = selectedCategoryType,
                        dailySummaries = dailySummaries
                    )
                }.collect { combinedUiState ->
                    _uiState.value = combinedUiState
                }

            } catch (e: Exception) {
                _uiState.value = TransactionsUiState.Error(
                    message = e.message ?: "Unknown error",
                )
            }
        }
    }

    /**
     * Computes daily summaries: groups transactions by date (within the specified month)
     * and calculates total income and expense for each day.
     */
    private fun computeDailySummaries(
        transactions: List<Transaction>,
        categories: List<Category>,
        yearMonth: YearMonth
    ): List<DailySummary> {
        // Prepare the dates for the current month.
        val daysInMonth = (yearMonth.lengthOfMonth()..1).map { day ->
            LocalDate.of(yearMonth.year, yearMonth.month, day)
        }

        // Group transactions by date.
        val transactionsByDate = transactions.groupBy { it.timestamp.toLocalDate() }

        return daysInMonth.mapNotNull { date ->
            val dailyTransactions = transactionsByDate[date].orEmpty()
            // Only include days that have transactions.
            if (dailyTransactions.isNotEmpty()) {
                // Get income and expense category IDs.
                val incomeIds = categories.filter { it.type == CategoryType.INCOME }.map { it.id }.toSet()
                val expenseIds = categories.filter { it.type == CategoryType.EXPENSE }.map { it.id }.toSet()

                val incomeTotal = dailyTransactions.filter { it.categoryId in incomeIds }.sumOf { it.amount }
                val expenseTotal = dailyTransactions.filter { it.categoryId in expenseIds }.sumOf { it.amount }

                DailySummary(
                    date = date,
                    transactions = dailyTransactions,
                    income = incomeTotal,
                    expense = expenseTotal
                )
            } else {
                null
            }
        }
    }

    // Navigation actions to update the month.
    fun previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1)
        loadTransactions()
    }

    fun nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1)
        loadTransactions()
    }

    fun onCategorySelected(category: Category?) {
        selectedCategory = category
        loadTransactions()
    }

    fun onCategoryTypeSelected(type: CategoryType) {
        selectedCategoryType = type
        selectedCategory = null  // Reset selected category when type changes.
        loadTransactions()
    }

//    fun createOrUpdateTransaction(
//        id: Long?,
//        categoryId: Long,
//        amount: Int,
//        note: String? = null
//    ) {
//        viewModelScope.launch {
//            try {
//                val newTransaction = Transaction(
//                    id = id ?: 0L,
//                    categoryId = categoryId,
//                    amount = amount,
//                    note = note,
//                )
//                upsertTransactionUseCase(newTransaction)
//
//                loadTransactions()
//            } catch (e: Exception) {
//                _uiState.value = TransactionsUIState.Error(
//                    e.message ?: "Unable to upsert transaction"
//                )
//            }
//        }
//    }
//
//    fun deleteTransaction(transactionId: Long) {
//        viewModelScope.launch {
//            try {
//                deleteTransactionUseCase(transactionId)
//            } catch (e: Exception) {
//                _uiState.value = TransactionsUIState.Error(
//                    e.message ?: "Unable to delete transaction"
//                )
//            }
//        }
//    }
}
