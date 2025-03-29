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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

sealed interface TransactionFormEvent {
    object FetchData : TransactionFormEvent
    object DeleteTransaction : TransactionFormEvent
    object SaveTransaction : TransactionFormEvent
    object ContinueTransaction : TransactionFormEvent
    object CopyTransaction : TransactionFormEvent
    data class SetCategoryType(val categoryType: CategoryType = CategoryType.EXPENSE) : TransactionFormEvent
    data class SetCategory(val category: Category) : TransactionFormEvent
    data class SetDate(val localDate: LocalDate) : TransactionFormEvent
    data class SetTime(val localTime: LocalTime) : TransactionFormEvent
    data class SetAmount(val amount: String) : TransactionFormEvent
    data class SetNote(val note: String) : TransactionFormEvent
    data class ValidateForm(val form: TransactionForm) : TransactionFormEvent
    data class AddAmountChar(val key: String) : TransactionFormEvent
    object EraseAmountChar : TransactionFormEvent
    object ClearAmountChar : TransactionFormEvent
    object ChangeAmountPosNev : TransactionFormEvent
    object ChangeAmountRounding : TransactionFormEvent
}
@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase,
    private val upsertTransactionUseCase: UpsertTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Retrieve the transactionId from the SavedStateHandle
    private val transactionId: Long? = savedStateHandle["transactionId"]

    private val amountText = MutableStateFlow("")

    private val _transactionFormUiState = MutableStateFlow<TransactionFormUiState>(TransactionFormUiState.Loading())
    val transactionFormUiState: StateFlow<TransactionFormUiState> = _transactionFormUiState.asStateFlow()



    init {
        onEvent(TransactionFormEvent.FetchData)
    }

    fun onEvent(event: TransactionFormEvent) {
        when (event) {
            TransactionFormEvent.ContinueTransaction -> continueTransaction()
            TransactionFormEvent.CopyTransaction -> {
                TODO()
            }
            TransactionFormEvent.DeleteTransaction -> deleteTransaction()
            TransactionFormEvent.FetchData -> {
                fetchCategories()
                fetchData()
            }
            TransactionFormEvent.SaveTransaction -> saveTransaction()
            is TransactionFormEvent.SetAmount -> setAmount(event.amount)
            is TransactionFormEvent.SetCategory -> setCategory(event.category)
            is TransactionFormEvent.SetCategoryType -> setCategoryType(event.categoryType)
            is TransactionFormEvent.SetDate -> setDate(event.localDate)
            is TransactionFormEvent.SetTime -> setTime(event.localTime)
            is TransactionFormEvent.SetNote -> setNote(event.note)
            is TransactionFormEvent.ValidateForm -> validateForm(event.form)
            is TransactionFormEvent.AddAmountChar -> {
                val numericCharSequence = "0123456789"
                Log.d("TransactionFormViewModel", "value: ${event.key}")
                Log.d("TransactionFormViewModel", "is numeric: ${numericCharSequence.contains(event.key)}")
                if (numericCharSequence.contains(event.key)) {
                    if (amountText.value == "0") {
                        amountText.value = amountText.value.dropLast(1)
                    }
                    amountText.value += event.key
                }
                setAmount(amountText.value)
            }
            TransactionFormEvent.EraseAmountChar -> {
                amountText.value = amountText.value.dropLast(1)
                setAmount(amountText.value)
            }
            TransactionFormEvent.ClearAmountChar -> {
                amountText.value = ""
            }
            TransactionFormEvent.ChangeAmountPosNev -> {
                if (amountText.value.startsWith("-")){
                    amountText.value = amountText.value.drop(1)
                } else {
                    amountText.value = "-" + amountText.value
                }
                setAmount(amountText.value)
            }
            TransactionFormEvent.ChangeAmountRounding -> {
                if (!amountText.value.contains(".")){
                    val numericCharSequence = "0123456789"
                    if (amountText.value.isNotEmpty()){
                        if (numericCharSequence.contains(amountText.value)) {
                            amountText.value += "."
                        }
                    }
                } else {
                    if (amountText.value.endsWith(".")){
                        amountText.value = amountText.value.dropLast(1)
                    }
                }
                setAmount(amountText.value)
            }
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
                    _transactionFormUiState.update { currentState ->
                        TransactionFormUiState.Error(
                            e.message ?: "Error loading categories",
                            categories = currentState.categories
                        )
                    }
                }
                .collect { categories ->
                    _transactionFormUiState.update { currentState ->
                        when (currentState) {
                            is TransactionFormUiState.Loading -> {
                                currentState.copy(categories = categories)
                            }

                            is TransactionFormUiState.Success -> {
                                currentState.copy(categories = categories)
                            }
                            else -> {
                                TransactionFormUiState.Loading(categories = categories)
                            }
                        }
                    }
                }
        }
    }

    private fun fetchData() {
        transactionId?.let { id ->
            if (id == 0L) {
                _transactionFormUiState.update { currentState ->
                    Log.d("TransactionFormViewModel", "currentState categories: ${currentState.categories}")
                    TransactionFormUiState.Empty(categories = currentState.categories)
                }
                return
            }
            viewModelScope.launch {
                combine(
                    getTransactionDetailsUseCase(id),
                    getCategoriesUseCase(),
                ) { transactionDetails, categories ->
                    Log.d("TransactionFormViewModel", "Fetched categories: $categories")
                    TransactionFormUiState.Success(
                        transactionData = TransactionData(
                            categoryType = transactionDetails.category.type,
                            category = transactionDetails.category,
                            dateTime = transactionDetails.transaction.timestamp,
                            amount = transactionDetails.transaction.amount.toString(),
                            note = transactionDetails.transaction.note.orEmpty(),
                        ),
                        formState = FormState(
                            transactionForm = TransactionForm(
                                categoryType = transactionDetails.category.type,
                                category = transactionDetails.category,
                                dateTime = transactionDetails.transaction.timestamp,
                                amount = transactionDetails.transaction.amount.toString(),
                                note = transactionDetails.transaction.note.orEmpty(),
                            ),
                            isNew = false,
                        ),
                        categories = categories
                    )
                }
                    .catch { e ->
                        _transactionFormUiState.update { currentState ->
                            TransactionFormUiState.Error(
                                message = e.message ?: "Failed to load transaction",
                                categories = currentState.categories
                            )
                        }
                    }
                    .collect { combinedUiState ->
                        _transactionFormUiState.update { combinedUiState }
                        Log.d("TransactionFormViewModel", "combinedUiState categories: ${combinedUiState.categories}")
                    }
            }
        }
    }

    // Handle category type change
    private fun setCategoryType(categoryType: CategoryType) {
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            TransactionFormUiState.Success(
                formState = currentForm.copy(transactionForm = currentForm.transactionForm.copy(categoryType = categoryType), isEditMode = true),
                categories = currentState.categories
            )
        }
    }

    // Handle category change
    private fun setCategory(category: Category?) {
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            TransactionFormUiState.Success(
                formState = currentForm.copy(transactionForm = currentForm.transactionForm.copy(category = category), isEditMode = true),
                categories = currentState.categories
            )
        }
    }

    // Handle date and time change
    private fun setDate(localDate: LocalDate) {
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            val oldLocalTime = currentForm.transactionForm.dateTime.toLocalTime()
            val newDateTime = LocalDateTime.of(localDate, oldLocalTime)
            TransactionFormUiState.Success(
                formState = currentForm.copy(transactionForm = currentForm.transactionForm.copy(dateTime = newDateTime), isEditMode = true),
                categories = currentState.categories
            )
        }
    }
    private fun setTime(localTime: LocalTime) {
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            val oldLocalDate = currentForm.transactionForm.dateTime.toLocalDate()
            val newDateTime = LocalDateTime.of(oldLocalDate, localTime)
            TransactionFormUiState.Success(
                formState = currentForm.copy(transactionForm = currentForm.transactionForm.copy(dateTime = newDateTime), isEditMode = true),
                categories = currentState.categories
            )
        }
    }

    // Handle amount change
    private fun setAmount(amount: String) {
        amountText.value = amount
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            val parsedAmount = amount.toDoubleOrNull()
            if (parsedAmount == null || parsedAmount <= 0) {
                TransactionFormUiState.Success(
                    formState = currentForm.copy(transactionForm = currentForm.transactionForm.copy(amount = amount), amountError = "Invalid amount", isEditMode = true),
                    categories = currentState.categories
                )
            } else {
                TransactionFormUiState.Success(
                    formState = currentForm.copy(transactionForm = currentForm.transactionForm.copy(amount = amount), amountError = null, isEditMode = true),
                    categories = currentState.categories
                )
            }

        }
    }

    // Handle note change
    private fun setNote(note: String) {
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            TransactionFormUiState.Success(
                formState = currentForm.copy(transactionForm = currentForm.transactionForm.copy(note = note), isEditMode = true),
                categories = currentState.categories
            )
        }
    }

    private fun deleteTransaction() {
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            val originalData = (currentState as? TransactionFormUiState.Success)?.transactionData
                ?: TransactionData(
                    categoryType = currentForm.transactionForm.categoryType,
                    category = currentForm.transactionForm.category,
                    dateTime = currentForm.transactionForm.dateTime,
                    amount = currentForm.transactionForm.amount,
                    note = currentForm.transactionForm.note,
                    isDeleted = currentForm.transactionForm.isDeleted
                )
            TransactionFormUiState.Success(
                formState = currentForm.copy(
                    transactionForm = currentForm.transactionForm.copy(
                        categoryType = originalData.categoryType,
                        category = originalData.category,
                        dateTime = originalData.dateTime,
                        amount = originalData.amount,
                        note = originalData.note,
                        isDeleted = true
                    )
                ),
                categories = currentState.categories
            )
        }
        saveTransaction()
    }

    private fun continueTransaction() {
        saveTransaction()
        _transactionFormUiState.update { currentState ->
            val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
            TransactionFormUiState.Success(
                formState = FormState(
                    transactionForm = currentForm.transactionForm.copy(
                        dateTime = currentForm.transactionForm.dateTime.plusSeconds(1),
                        amount = "",
                        note = "",
                        isDeleted = false
                    )
                ),
                categories = currentState.categories
            )
        }
    }

    // Save transaction to the repository
    private fun saveTransaction() {
        Log.d("TransactionFormViewModel", "Saving transaction")
        val currentForm = (transactionFormUiState.value as? TransactionFormUiState.Success)?.formState ?: (transactionFormUiState.value as? TransactionFormUiState.Loading)?.formState ?: FormState()

        if (validateForm(currentForm.transactionForm)) {
            viewModelScope.launch {
                try {
                    val transaction = Transaction(
                        id = transactionId ?: 0L, // Use transactionId if available
                        categoryId = currentForm.transactionForm.category!!.id,
                        accountId = 1L, // Replace with actual account ID
                        amount = currentForm.transactionForm.amount.toDouble(),
                        note = currentForm.transactionForm.note,
                        timestamp = currentForm.transactionForm.dateTime,
                        isDeleted = currentForm.transactionForm.isDeleted,

                    )
                    upsertTransactionUseCase(transaction)

                } catch (e: Exception) {
                    val errorMsg = e.message ?: "Failed to save transaction"
                    Log.d("TransactionFormViewModel", "Saving transaction failed : $errorMsg")
                }
            }
        }
    }


    // Validate form data
    fun validateForm(form: TransactionForm): Boolean {
        var isValid = true
        if (form.amount.isBlank()) {
            _transactionFormUiState.update { currentState ->
                val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
                TransactionFormUiState.Success(
                    formState = currentForm.copy(amountError = "Please enter a number"),
                    categories = currentState.categories
                )
            }
            isValid = false
        } else if (form.amount.toDouble() <= 0) {
            _transactionFormUiState.update { currentState ->
                val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
                TransactionFormUiState.Success(
                    formState = currentForm.copy(amountError = "Invalid Amount"),
                    categories = currentState.categories
                )
            }
            isValid = false
        }
        if (form.category == null) {
            _transactionFormUiState.update { currentState ->
                val currentForm = (currentState as? TransactionFormUiState.Success)?.formState ?: (currentState as? TransactionFormUiState.Loading)?.formState ?: FormState()
                TransactionFormUiState.Success(
                    formState = currentForm.copy(categoryError = "Please select a category"),
                    categories = currentState.categories
                )
            }
            isValid = false
        }
        return isValid
    }
}
