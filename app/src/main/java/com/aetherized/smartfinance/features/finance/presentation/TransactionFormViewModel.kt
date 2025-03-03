package com.aetherized.smartfinance.features.finance.presentation

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
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

    private val transactionId: Long? = savedStateHandle["transactionId"]

    private val _uiState = MutableStateFlow<TransactionFormUiState>(TransactionFormUiState.Initial)
    val uiState: StateFlow<TransactionFormUiState> = _uiState.asStateFlow()

    init {
        loadTransaction()
//        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .collect { categories ->
                    _uiState.update { currentState ->
                        if (currentState is TransactionFormUiState.FormState) {
                            currentState.copy(categories = categories)
                        } else {
                            TransactionFormUiState.FormState(categories = categories)
                        }
                    }
                }
        }
    }

    private fun loadTransaction() {
        transactionId?.let { id ->
            viewModelScope.launch {
                _uiState.value = TransactionFormUiState.Loading
                try {
                    combine(
                        getTransactionDetailsUseCase(id),
                        getCategoriesUseCase(),
                    ) { transactionDetails, categories ->
                        _uiState.value = TransactionFormUiState.FormState(
                            categoryType = transactionDetails.category.type,
                            category = transactionDetails.category,
                            dateTime = transactionDetails.transaction.timestamp,
                            amount = transactionDetails.transaction.amount,
                            note = transactionDetails.transaction.note.orEmpty(),
                            categories = categories
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = TransactionFormUiState.Error(e.message ?: "Failed to load transaction")
                }
            }
        }
    }

    // Update form state based on user input
    fun updateFormState(newState: TransactionFormUiState.FormState) {
        _uiState.value = newState
    }

    // Handle category type change
    fun onCategoryTypeChanged(categoryType: CategoryType) {
        _uiState.update { currentState ->
            if (currentState is TransactionFormUiState.FormState) {
                currentState.copy(categoryType = categoryType, category = null)
            } else {
                TransactionFormUiState.FormState(categoryType = categoryType)
            }
        }
    }

    // Handle category change
    fun onCategoryChanged(category: Category?) {
        _uiState.update { currentState ->
            if (currentState is TransactionFormUiState.FormState) {
                currentState.copy(category = category)
            } else {
                TransactionFormUiState.FormState(category = category)
            }
        }
    }

    // Handle date and time change
    fun onDateTimeChanged(dateTime: LocalDateTime) {
        _uiState.update { currentState ->
            if (currentState is TransactionFormUiState.FormState) {
                currentState.copy(dateTime = dateTime)
            } else {
                TransactionFormUiState.FormState(dateTime = dateTime)
            }
        }
    }

    // Handle amount change
    fun onAmountChanged(amount: String) {
        _uiState.update { currentState ->
            if (currentState is TransactionFormUiState.FormState) {
                currentState.copy(amount = amount.toDouble(), amountError = null)
            } else {
                TransactionFormUiState.FormState(amount = amount.toDouble())
            }
        }
    }

    // Handle note change
    fun onNoteChanged(note: String) {
        _uiState.update { currentState ->
            if (currentState is TransactionFormUiState.FormState) {
                currentState.copy(note = note)
            } else {
                TransactionFormUiState.FormState(note = note)
            }
        }

    }

    fun onDeleteClicked() {
        _uiState.update { currentState ->
            if (currentState is TransactionFormUiState.FormState) {
                currentState.copy(isDeleted = true)
            } else {
                TransactionFormUiState.FormState(isDeleted = true)
            }
        }
    }

    fun continueTransaction() {
        _uiState.update { currentState ->
            (currentState as TransactionFormUiState.FormState).copy(
                    amount = 0.0,
                    note = "",
                    amountError = null,
                    categoryError = null,
                    isDeleted = false
                )
        }
    }

    // Save transaction to the repository
    fun saveTransaction() {
        val currentState = _uiState.value as? TransactionFormUiState.FormState ?: return
        if (validateForm(currentState)) {
            viewModelScope.launch {
                _uiState.value = TransactionFormUiState.Loading
                try {
                    val transaction = Transaction(
                        id = transactionId ?: 0L, // Use transactionId if available
                        categoryId = currentState.category!!.id,
                        accountId = 1L, // Replace with actual account ID
                        amount = currentState.amount.toDouble(),
                        note = currentState.note,
                        timestamp = currentState.dateTime,
                        isDeleted = currentState.isDeleted
                    )
                    upsertTransactionUseCase(transaction)
                    _uiState.value = TransactionFormUiState.Success(transaction)
                } catch (e: Exception) {
                    _uiState.value = TransactionFormUiState.Error(e.message ?: "Failed to save transaction")
                }
            }
        }
    }


    // Validate form data
    private fun validateForm(state: TransactionFormUiState.FormState): Boolean {
        var isValid = true
        if (state.amount <= 0) {
            _uiState.value = state.copy(amountError = "Invalid amount")
            isValid = false
        }
        if (state.category == null) {
            _uiState.value = state.copy(categoryError = "Please select a category")
            isValid = false
        }
        return isValid
    }
}
//
//
//    // Retrieve the transaction ID from the savedStateHandle (passed via navigation)
//    private val transactionId: Long = savedStateHandle["transactionId"] ?: 0L
//
//    private var selectedCategoryType: CategoryType = CategoryType.EXPENSE
//
//
//    // Holds the transaction for edit mode; null in create mode.
//    private val _categories = MutableStateFlow<List<Category>>(listOf())
//    val categories: List<Category> get() = _categories.value
//
//    init {
//        loadTransaction()
//        loadSelectedCategoryType()
//    }
//
//
//    private fun loadTransaction() {
//        if (transactionId == 0L) {
//            viewModelScope.launch {
//                try {
//                    combine(
//                        getTransactionDetailsUseCase(transactionId),
//                        getCategoriesUseCase(),
//                    ) { transactionDetails, categories ->
//                        _uiState.value = TransactionFormUiState.Success(
//                            transaction = transactionDetails.transaction,
//                            category = transactionDetails.category,
//                            allCategories = categories
//                        )
//                    }
//                    getCategoriesUseCase()
//                        .collect { categories ->
//                            _uiState.value = TransactionFormUiState.Create(
//                                allCategories = categories
//                            )
//                        }
//                } catch (e: Exception) {
//                    _uiState.value = TransactionFormUiState.Error(
//                        message = e.message ?: "Unknown error",
//                    )
//                }
//
//            } else {
////                viewModelScope.launch {
////                    try {
////                        combine(
////                            getTransactionDetailsUseCase(transactionId),
////                            getCategoriesUseCase(),
////                        ) { transactionDetails, categories ->
////                            _uiState.value = TransactionFormUiState.Success(
////                                transaction = transactionDetails.transaction,
////                                category = transactionDetails.category,
////                                allCategories = categories
////                            )
////                        }
////                    } catch (e: Exception) {
////                        _uiState.value = TransactionFormUiState.Error(
////                            message = e.message ?: "Unknown error",
////                        )
////                    }
//                }
//            }
//        }
//    }
//    private fun loadSelectedCategoryType() {
////        _categories.value = uiState.value.let {
////            if (it is TransactionFormUiState.Success) {
////                it.allCategories.filter { category ->
////                    category.type == selectedCategoryType
////                }
////            } else {
////                emptyList()
////            }
////        }
//    }
//
//    fun saveTransaction(updatedTransaction: Transaction, onResult: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            try {
//                upsertTransactionUseCase(updatedTransaction)
//                Log.d("TransactionFormVM", "TransactionFormVM: saveTransaction Success?")
//                // Reload details after update.
//                loadTransaction()
//                onResult(true)
//            } catch (e: Exception) {
//                Log.d("TransactionFormVM", "TransactionFormVM: saveTransaction Failed?")
//                _uiState.value = TransactionFormUiState.Error(e.message ?: "Update failed")
//                onResult(false)
//            }
//        }
//    }
//
//    fun deleteTransaction(onResult: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            try {
//                deleteTransactionUseCase(transactionId)
//                onResult(true)
//                // TODO: signal success via an event or navigation callback.
//            } catch (e: Exception) {
//                _uiState.value = TransactionFormUiState.Error(e.message ?: "Delete failed")
//                onResult(false)
//            }
//        }
//    }
//
//    fun copyTransaction() {
//        // Implement copy behavior.
//    }
//
//    fun pickDateTime(current: LocalDateTime, onDateTimeSelected: (LocalDateTime) -> Unit) {
//        // Trigger your date/time picker logic. For example, show a dialog.
//        // For now, we'll just call the callback with a new value.
//        onDateTimeSelected(current.plusHours(1))
//    }
//}
