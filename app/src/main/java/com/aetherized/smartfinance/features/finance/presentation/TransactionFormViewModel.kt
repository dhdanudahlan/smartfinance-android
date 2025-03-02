package com.aetherized.smartfinance.features.finance.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteTransactionUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetTransactionDetailsUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase,
    private val upsertTransactionUseCase: UpsertTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransactionFormUIState>(
        TransactionFormUIState.Create
    )
    val uiState: StateFlow<TransactionFormUIState> = _uiState


    // Retrieve the transaction ID from the savedStateHandle (passed via navigation)
    private val transactionId: Long = savedStateHandle["transactionId"] ?: -1

    private var selectedCategoryType: CategoryType = CategoryType.EXPENSE


    // Holds the transaction for edit mode; null in create mode.
    private val _categories = MutableStateFlow<List<Category>>(listOf())
    val categories: List<Category> get() = _categories.value

    init {
        loadTransaction()
        loadSelectedCategoryType()
    }


    private fun loadTransaction() {
        if (transactionId == -1L) {
            viewModelScope.launch {
                try {
                    combine(
                        getTransactionDetailsUseCase(transactionId),
                        getCategoriesUseCase(),
                    ) { transactionDetails, categories ->
                        _uiState.value = TransactionFormUIState.Success(
                            transaction = transactionDetails.transaction,
                            category = transactionDetails.category,
                            allCategories = categories
                        )
                    }
                    getCategoriesUseCase()
                        .collect { categories ->
                            _uiState.value = TransactionFormUIState.Create(
                                allCategories = categories
                            )
                        }
                } catch (e: Exception) {
                    _uiState.value = TransactionFormUIState.Error(
                        message = e.message ?: "Unknown error",
                    )
                }

            } else {
            viewModelScope.launch {
                try {
                    combine(
                        getTransactionDetailsUseCase(transactionId),
                        getCategoriesUseCase(),
                    ) { transactionDetails, categories ->
                        _uiState.value = TransactionFormUIState.Success(
                            transaction = transactionDetails.transaction,
                            category = transactionDetails.category,
                            allCategories = categories
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = TransactionFormUIState.Error(
                        message = e.message ?: "Unknown error",
                    )
                }
            }
        }
    }
    private fun loadSelectedCategoryType() {
        _categories.value = uiState.value.let {
            if (it is TransactionFormUIState.Success) {
                it.allCategories.filter { category ->
                    category.type == selectedCategoryType
                }
            } else {
                emptyList()
            }
        }
    }

    fun saveTransaction(updatedTransaction: Transaction, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                upsertTransactionUseCase(updatedTransaction)
                Log.d("TransactionFormVM", "TransactionFormVM: saveTransaction Success?")
                // Reload details after update.
                loadTransaction()
                onResult(true)
            } catch (e: Exception) {
                Log.d("TransactionFormVM", "TransactionFormVM: saveTransaction Failed?")
                _uiState.value = TransactionFormUIState.Error(e.message ?: "Update failed")
                onResult(false)
            }
        }
    }

    fun deleteTransaction(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(transactionId)
                onResult(true)
                // TODO: signal success via an event or navigation callback.
            } catch (e: Exception) {
                _uiState.value = TransactionFormUIState.Error(e.message ?: "Delete failed")
                onResult(false)
            }
        }
    }

    fun copyTransaction() {
        // Implement copy behavior.
    }

    fun pickDateTime(current: LocalDateTime, onDateTimeSelected: (LocalDateTime) -> Unit) {
        // Trigger your date/time picker logic. For example, show a dialog.
        // For now, we'll just call the callback with a new value.
        onDateTimeSelected(current.plusHours(1))
    }
}
