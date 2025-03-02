package com.aetherized.smartfinance.features.finance.presentation.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormUIState
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun TransactionFormScreenContainer(
    transactionId: Long?, // null means create mode; non-null means edit mode.
    navController: NavController,
    viewModel: TransactionFormViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }


    // Get all available categories (could be from a shared ViewModel or repository)
    // For simplicity, assume viewModel.allCategories holds that list.
    val allCategories = viewModel.categories

    when (uiState) {
        is TransactionFormUIState.Create -> {
            val createState = uiState as TransactionFormUIState.Create
            TransactionFormScreen (
                transaction = null,
                allCategories = createState.allCategories,
                onSave = { newTransaction, continueAfterSave ->
                    viewModel.saveTransaction(newTransaction) { success ->
                        if (success && !continueAfterSave) {
                            navController.popBackStack()
                        }
                    }
                },
                onDelete = {
                    viewModel.deleteTransaction {
                        navController.popBackStack()
                    }
                },
                onCopy = {
                    viewModel.copyTransaction()
                },
                onCancel = {
                    navController.popBackStack()
                },
                onPickDateTime = { current, onDateTimeSelected ->
                    viewModel.pickDateTime(current, onDateTimeSelected)
                }
            )
        }
        is TransactionFormUIState.Success -> {
            // Pass all necessary data to the detailed screen.
            val successState = uiState as TransactionFormUIState.Success
            TransactionFormScreen (
                transaction = successState.transaction,
                allCategories = successState.allCategories,
                onSave = { newTransaction, continueAfterSave ->
                    viewModel.saveTransaction(newTransaction) { success ->
                        if (success && !continueAfterSave) {
                            navController.popBackStack()
                        }
                    }
                },
                onDelete = {
                    viewModel.deleteTransaction {
                        navController.popBackStack()
                    }
                },
                onCopy = {
                    viewModel.copyTransaction()
                },
                onCancel = {
                    navController.popBackStack()
                },
                onPickDateTime = { current, onDateTimeSelected ->
                    viewModel.pickDateTime(current, onDateTimeSelected)
                }
            )
        }
        is TransactionFormUIState.Error -> {
            val errorState = uiState as TransactionFormUIState.Error
            Log.d("TransactionDetailsUIState.Error.Screen", errorState.message)
            // Display an error message on-screen.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as TransactionFormUIState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
/**
 * A reusable composable screen for creating and editing a transaction.
 *
 * @param transaction The existing transaction to edit, or null for creating a new one.
 * @param allCategories List of all available categories.
 * @param onSave Callback invoked when the user saves the transaction.
 *               The Boolean parameter indicates whether to continue (for create mode).
 * @param onDelete Optional callback invoked when the user chooses to delete an existing transaction.
 * @param onCopy Optional callback invoked when the user chooses to copy a transaction.
 * @param onCancel Callback invoked when the user cancels the operation.
 * @param onPickDateTime Callback to show a date/time picker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormScreen(
    transaction: Transaction?, // null => create mode; non-null => edit mode.
    allCategories: List<Category>,
    onSave: (Transaction, continueAfterSave: Boolean) -> Unit,
    onDelete: (() -> Unit)? = null,
    onCopy: (() -> Unit)? = null,
    onCancel: () -> Unit,
    onPickDateTime: (current: LocalDateTime, onDateTimeSelected: (LocalDateTime) -> Unit) -> Unit
) {
    // Determine mode.
    val isEditMode = transaction != null
    var isEditing by remember { mutableStateOf(false) }

    // Screen title and button text.
    val screenTitle = if (isEditMode) "Edit Transaction" else "New Transaction"

    // Form state variables.
    var selectedType by remember {
        mutableStateOf(
            transaction?.let {
                allCategories.find { cat -> cat.id == it.categoryId }?.type ?: CategoryType.EXPENSE
            } ?: CategoryType.EXPENSE
        )
    }
    var selectedCategory by remember {
        mutableStateOf(
            transaction?.let {
                allCategories.find { cat -> cat.id == it.categoryId }
            }
        )
    }
    var selectedDateTime by remember { mutableStateOf(transaction?.timestamp ?: LocalDateTime.now()) }
    var amountText by remember { mutableStateOf(transaction?.amount?.toString() ?: "") }
    var noteText by remember { mutableStateOf(transaction?.note ?: "") }

    // State for showing the category selection bottom sheet.
    var showCategorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Filter categories based on selectedType.
    val filteredCategories = allCategories.filter { it.type == selectedType }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Transaction Type Selector using FilterChips.
            TransactionTypeSelector(
                selectedType = selectedType,
                onTypeSelected = { newType ->
                    selectedType = newType
                    selectedCategory = null // Reset category when type changes.
                    showCategorySheet = true
                },
                onEditing = { isEditing = true }
            )

            // Category Selection Field.
            OutlinedTextField(
                value = selectedCategory?.name ?: "Select category",
                onValueChange = { /* Read-only */ },
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategorySheet = true }
            )

            // Date & Time Input Field.
            OutlinedTextField(
                value = selectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                onValueChange = { /* Read-only */ },
                readOnly = true,
                label = { Text("Date & Time") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onPickDateTime(selectedDateTime) { picked ->
                            selectedDateTime = picked
                        }
                    }
            )

            // Amount Input Field.
            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    isEditing = true
                },
                label = { Text("Amount") },
                placeholder = { Text("Enter amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Note Input Field.
            OutlinedTextField(
                value = noteText,
                onValueChange = {
                    noteText = it
                    isEditing = true
                },
                label = { Text("Note (Optional)") },
                placeholder = { Text("Enter note") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons.
            if (!isEditMode) {
                // Create Mode: "Save" and "Save & Continue" buttons.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            val amountValue = amountText.toIntOrNull() ?: 0
                            val newTransaction = Transaction(
                                id = -1,
                                categoryId = selectedCategory?.id ?: 0,
                                amount = amountValue,
                                note = noteText,
                                timestamp = selectedDateTime
                            )
                            Log.d("TransactionFormScreen", "TransactionFormScreen: !isEditMode Save")
                            onSave(newTransaction, false)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amountValue = amountText.toIntOrNull() ?: 0
                            val newTransaction = Transaction(
                                id = -1,
                                categoryId = selectedCategory?.id ?: 0,
                                amount = amountValue,
                                note = noteText,
                                timestamp = selectedDateTime
                            )
                            Log.d("TransactionFormScreen", "TransactionFormScreen: !isEditMode Continue")
                            onSave(newTransaction, true)
                            // Reset fields for continued entry.
                            amountText = ""
                            noteText = ""
                            selectedDateTime = LocalDateTime.now()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Continue")
                    }
                }
            } else {
                // Edit Mode: When not editing, show "Delete" and "Copy". Once editing, show "Save".
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!isEditing) {
                        Button(
                            onClick = { onDelete?.invoke() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onCopy?.invoke() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Copy")
                        }
                    } else {
                        Button(
                            onClick = {
                                val amountValue = amountText.toIntOrNull() ?: 0
                                val updatedTransaction = Transaction(
                                    id = transaction!!.id,
                                    categoryId = selectedCategory?.id ?: transaction.categoryId,
                                    amount = amountValue,
                                    note = noteText,
                                    timestamp = selectedDateTime
                                )
                                Log.d("TransactionFormScreen", "TransactionFormScreen: isEditMode Save")
                                onSave(updatedTransaction, false)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet for Category Selection.
    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Select Category", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(filteredCategories) { category ->
                        ListItem(
                            headlineContent = { Text(category.name) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategory = category
                                    showCategorySheet = false
                                }
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable for selecting transaction type using Material3 FilterChips.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionTypeSelector(
    selectedType: CategoryType,
    onTypeSelected: (CategoryType) -> Unit,
    onEditing: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.weight(1f)) {
            FilterChip(
                selected = selectedType == CategoryType.INCOME,
                onClick = {
                    onTypeSelected(CategoryType.INCOME)
                    onEditing()
                },
                label = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Income") } },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = MaterialTheme.colorScheme.secondary,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = selectedType == CategoryType.INCOME,
                    selected = selectedType == CategoryType.INCOME,
                    borderColor = MaterialTheme.colorScheme.onSurface,
                    selectedBorderColor = MaterialTheme.colorScheme.secondary,
                    selectedBorderWidth = 1.dp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            FilterChip(
                selected = selectedType == CategoryType.EXPENSE,
                onClick = {
                    onTypeSelected(CategoryType.EXPENSE)
                    onEditing()
                },
                label = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Expense") } },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = MaterialTheme.colorScheme.secondary,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = selectedType == CategoryType.EXPENSE,
                    selected = selectedType == CategoryType.EXPENSE,
                    borderColor = MaterialTheme.colorScheme.onSurface,
                    selectedBorderColor = MaterialTheme.colorScheme.secondary,
                    selectedBorderWidth = 1.dp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
