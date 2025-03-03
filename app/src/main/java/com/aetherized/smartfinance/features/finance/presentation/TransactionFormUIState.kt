package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import java.time.LocalDateTime

sealed class TransactionFormUiState {
    object Initial : TransactionFormUiState() // Initial state when the form is first loaded
    object Loading : TransactionFormUiState() // Indicates that data is being loaded or saved
    data class Success(val transaction: Transaction? = null) : TransactionFormUiState() // Transaction saved/updated successfully
    data class Error(val message: String) : TransactionFormUiState() // Error occurred during data loading or saving
    data class FormState(
        val categoryType: CategoryType = CategoryType.EXPENSE, // Default to expense
        val category: Category? = null, // Selected category
        val dateTime: LocalDateTime = LocalDateTime.now(), // Selected date and time
        val amount: Double = 0.0, // Amount entered by the user
        val note: String = "", // Note entered by the user
        val categories: List<Category> = emptyList(), // List of available categories
        val amountError: String? = null, // Error message for amount field
        val categoryError: String? = null, // Error message for category field
        val isDeleted: Boolean = false // Indicates if the transaction is deleted
    ) : TransactionFormUiState() // State containing form data and validation errors
}
