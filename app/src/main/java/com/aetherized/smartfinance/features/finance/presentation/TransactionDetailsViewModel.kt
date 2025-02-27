package com.aetherized.smartfinance.features.finance.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteTransactionUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetTransactionDetailsUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase,
    private val upsertTransactionUseCase: UpsertTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<TransactionDetailsUIState>(
        TransactionDetailsUIState.Loading
    )
    val uiState: StateFlow<TransactionDetailsUIState> = _uiState

    // Retrieve the transaction ID from the savedStateHandle (passed via navigation)
    private val transactionId: Long = savedStateHandle["transactionId"] ?: -1

    private var selectedCategoryType: CategoryType = CategoryType.EXPENSE
    private var currentYearMonth: YearMonth = YearMonth.now()

    init {
        Log.d("UI", "TransactionDetailsViewModel init")
        loadTransactionDetails()
    }

    private fun loadTransactionDetails() {
        _uiState.value = TransactionDetailsUIState.Loading

        viewModelScope.launch {
            try {
                getTransactionDetailsUseCase(transactionId)
                    .collect { details ->
                        _uiState.value = TransactionDetailsUIState.Success(
                            transaction = details.transaction,
                            category = details.category
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = TransactionDetailsUIState.Error(
                    message = e.message ?: "Unknown error",
                )
            }
        }
    }

    fun updateTransaction(updatedTransaction: Transaction) {
        viewModelScope.launch {
            try {
                upsertTransactionUseCase(updatedTransaction)
                // Reload details after update.
                loadTransactionDetails()
            } catch (e: Exception) {
                _uiState.value = TransactionDetailsUIState.Error(e.message ?: "Update failed")
            }
        }
    }
    fun deleteTransaction() {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(transactionId)
                // TODO: signal success via an event or navigation callback.
            } catch (e: Exception) {
                _uiState.value = TransactionDetailsUIState.Error(e.message ?: "Delete failed")
            }
        }
    }
}
