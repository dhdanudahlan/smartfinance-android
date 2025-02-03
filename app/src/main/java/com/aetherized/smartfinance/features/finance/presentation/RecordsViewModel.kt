package com.aetherized.smartfinance.features.finance.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteTransactionUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetTransactionsUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val upsertTransactionUseCase: UpsertTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<RecordsUIState>(
        RecordsUIState.Loading(selectedCategoryType = CategoryType.EXPENSE)
    )
    val uiState: StateFlow<RecordsUIState> = _uiState

    private var selectedCategory: Category? = null
    private var selectedCategoryType: CategoryType = CategoryType.EXPENSE

    init {
        Log.d("UI", "RecordsViewModel init")
        loadRecords(selectedCategoryType, selectedCategory)
    }

    private fun loadRecords(type: CategoryType, category: Category?, limit: Int = 50, offset: Int = 0) {
        _uiState.value = RecordsUIState.Loading(type)
        selectedCategoryType = type
        selectedCategory = category

        viewModelScope.launch {
            try {
                getTransactionsUseCase(
                    categoryId = category?.id,
                    categoryType = if (category == null) type else null,
                    limit = limit,
                    offset = offset
                ).collect { transactions ->
                    getCategoriesUseCase(
                        categoryType = selectedCategoryType,
                        limit = limit,
                        offset = offset
                    ).collect { categories ->
                        _uiState.value = RecordsUIState.Success(
                            transactions = transactions,
                            categories = categories,
                            selectedCategory = category,
                            selectedCategoryType = type
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RecordsUIState.Error(
                    message = e.message ?: "Unknown error",
                )
            }
        }
    }

    fun createOrUpdateTransaction(id: Long?, categoryId: Long, amount: Double, note: String? = null, type: CategoryType) {
        viewModelScope.launch {
            try {
                val newTransaction = Transaction(
                    id = id ?: -1,
                    categoryId = categoryId,
                    amount = amount,
                    note = note
                )
                upsertTransactionUseCase(newTransaction)
            } catch (e: Exception) {
                _uiState.value = RecordsUIState.Error(
                    e.message ?: "Unable to upsert transaction"
                )
            }
        }
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(transactionId)
            } catch (e: Exception) {
                _uiState.value = RecordsUIState.Error(
                    e.message ?: "Unable to delete transaction"
                )
            }
        }
    }

    fun onCategorySelected(category: Category?) {
        loadRecords(selectedCategoryType, category)
    }

    fun onCategoryTypeSelected(type: CategoryType) {
        loadRecords(type, null)
    }
}
