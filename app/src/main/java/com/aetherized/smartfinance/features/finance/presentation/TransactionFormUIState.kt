package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import java.time.LocalDateTime

data class FormState(
    val transactionForm: TransactionForm = TransactionForm(),
    val amountError: String? = null, // Error message for amount field
    val categoryError: String? = null, // Error message for category field
    val isNew: Boolean = true, // Indicates if the transaction is new
    val isEditMode: Boolean = false, // Indicates if the transaction is in edit mode
)
data class TransactionForm(
    val categoryType: CategoryType = CategoryType.EXPENSE, // Default to expense
    val category: Category? = null, // Selected category
    val dateTime: LocalDateTime = LocalDateTime.now(), // Selected date and time
    val amount: String = "", // Amount entered by the user
    val note: String = "", // Note entered by the user
    val isDeleted: Boolean = false, // Indicates if the transaction is deleted
)
data class TransactionData(
    val categoryType: CategoryType = CategoryType.EXPENSE, // Default to expense
    val category: Category? = null, // Selected category
    val dateTime: LocalDateTime = LocalDateTime.now(), // Selected date and time
    val amount: String = "", // Amount entered by the user
    val note: String = "", // Note entered by the user
    val isDeleted: Boolean = false, // Indicates if the transaction is deleted
)
sealed class TransactionFormUiState {
    abstract val formState: FormState
    abstract val categories: List<Category>
    data class Empty(
        override val formState: FormState = FormState(),
        override val categories: List<Category> = emptyList()
    ) : TransactionFormUiState()
    data class Loading(
        override val formState: FormState = FormState(),
        override val categories: List<Category> = emptyList()
    ) : TransactionFormUiState()
    data class Success(
        val transactionData: TransactionData = TransactionData(),
        override val formState: FormState = FormState(),
        override val categories: List<Category> = emptyList()
    ) : TransactionFormUiState()
    data class Error(
        val message: String,
        override val formState: FormState = FormState(),
        override val categories: List<Category> = emptyList()
    ) : TransactionFormUiState()
}

// Sealed class for one-time UI events
sealed class TransactionFormUiEvent {
    data class ShowError(val message: String) : TransactionFormUiEvent()
    object TransactionSaved : TransactionFormUiEvent()
}

