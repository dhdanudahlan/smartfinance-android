package com.aetherized.smartfinance.features.finance.presentation.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.presentation.FormState
import com.aetherized.smartfinance.features.finance.presentation.TransactionForm
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormEvent
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormUiState
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormViewModel
import com.aetherized.smartfinance.ui.theme.SmartFinanceTheme
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


@Composable
fun TransactionFormScreenContainer(
//    transactionId: Long? = null, // Null for new transaction, non-null for editing
    navigateToPrevious: () -> Unit, // Callback when transaction is saved
    viewModel: TransactionFormViewModel = hiltViewModel()
) {

    val transactionFormUiState by viewModel.transactionFormUiState.collectAsState()

    TransactionFormScreen(
        transactionsFormUiState = transactionFormUiState,
        onEvent = viewModel::onEvent,
        navigateToPrevious = navigateToPrevious,
        onTransactionSaved = { form ->
            if (viewModel.validateForm(form)) {
                navigateToPrevious()
            }
        }
    )
}

/**
* A reusable composable screen for creating and editing a transaction.
*
* @param transactionsFormUiState The existing state that contain data to edit.
* @param onEvent Callback invoked when an event happened.
**/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormScreen(
    transactionsFormUiState: TransactionFormUiState,
    onEvent: (TransactionFormEvent) -> Unit,
    navigateToPrevious: () -> Unit,
    onTransactionSaved: (TransactionForm) -> Unit
) {
    // Determine mode.
    var isEditMode by remember { mutableStateOf(transactionsFormUiState.formState.isEditMode) }

    // Screen title and button text.
    val screenTitle = transactionsFormUiState.formState.transactionForm.categoryType.name

    val filteredCategories by remember { mutableStateOf(transactionsFormUiState.categories) }

    // State for showing the category selection bottom sheet.
    var showCategorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val scope = rememberCoroutineScope()

    // Filter categories based on selectedType.
//    val filteredCategories = allCategories.filter { it.type == selectedType }

    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = navigateToPrevious) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        scaffoldState = scaffoldState,
        sheetContent = {
            val filteredCategory = transactionsFormUiState.categories.filter { it.type == transactionsFormUiState.formState.transactionForm.categoryType }
            CategoryBottomSheetContent(
                filteredCategory = filteredCategory,
                isCategorySheetSelected = {
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                },
                onEvent = onEvent
            )
        },
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(start = 16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        "Category",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.fillMaxHeight(),
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        sheetSwipeEnabled = false,
        sheetPeekHeight = 0.dp
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
                selectedType = transactionsFormUiState.formState.transactionForm.categoryType,
                onTypeSelected = { newType ->
                    onEvent(TransactionFormEvent.SetCategoryType(newType))
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                    filteredCategories.filter {
                        it.type == newType
                    }
                },
                onEditing = { isEditMode = true }
            )
            // Date & Time Input Field.
            TransactionFormItem(
                label = "Date",
                value = transactionsFormUiState.formState.transactionForm.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                onValueChange = { /* Read-only */ },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                    },
                trailingIcon = {
                    IconButton(onClick = { /* Implement date/time picker */ }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Date and Time", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )

            // Category Selection Field.
            TransactionFormItem(
                label = "Category",
                value = transactionsFormUiState.formState.transactionForm.category?.name,
                onValueChange = { /* Read-only */ },
                onFocused = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                onUnfocused = {
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                },
                readOnly = true,
                modifier = Modifier,
                trailingIcon = {
                    Icon(Icons.Rounded.Delete, contentDescription = "Amount", tint = Color.Transparent)
                }
            )


            // Amount Input Field.
            TransactionFormItem(
                label = "Amount",
                value = transactionsFormUiState.formState.transactionForm.amount,
                onValueChange = {
                    onEvent(TransactionFormEvent.SetAmount(it))
                    isEditMode = true
                },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { onEvent(TransactionFormEvent.SetAmount("")) }) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Amount")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )

            )

            // Note Input Field.
            TransactionFormItem(
                label = "Note",
                value = transactionsFormUiState.formState.transactionForm.note,
                onValueChange = {
                    onEvent(TransactionFormEvent.SetNote(it))
                    isEditMode = true
                },
                placeholder = { Text(text = "Optional", style = MaterialTheme.typography.bodySmall, color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { onEvent(TransactionFormEvent.SetNote("")) }) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Amount")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons.
            if (transactionsFormUiState.formState.isNew) {
                // Create Mode: "Save" and "Save & Continue" buttons.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            onEvent(TransactionFormEvent.SaveTransaction)
                            onTransactionSaved(transactionsFormUiState.formState.transactionForm)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onEvent(TransactionFormEvent.ContinueTransaction)
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
                    if (!isEditMode) {
                        Button(
                            onClick = {
                                onEvent(TransactionFormEvent.DeleteTransaction)
                                onTransactionSaved(transactionsFormUiState.formState.transactionForm)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onEvent(TransactionFormEvent.CopyTransaction)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Copy")
                        }
                    } else {
                        Button(
                            onClick = {
                                onEvent(TransactionFormEvent.SaveTransaction)
                                onTransactionSaved(transactionsFormUiState.formState.transactionForm)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormItem(
    label: String,
    modifier: Modifier = Modifier,
    value: String?,
    onValueChange: (String) -> Unit,
    onFocused: () -> Unit = {},
    onUnfocused: () -> Unit = {},
    readOnly: Boolean = false,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    supportingText: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        val interactionSource = remember { MutableInteractionSource() }
        val visualTransformation = VisualTransformation.None
        val cursorBrush = SolidColor(Color.Black)
        val textStyle = MaterialTheme.typography.bodySmall
        Row(
            modifier = Modifier.weight(3f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value.orEmpty(),
                onValueChange = onValueChange,
                readOnly = readOnly,
                modifier = modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onFocused()
                        } else {
                            onUnfocused()
                        }
                    },
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                cursorBrush = cursorBrush,
                interactionSource = interactionSource
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value.orEmpty(),
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    enabled = true,
                    singleLine = true,
                    isError = isError,
                    supportingText = supportingText,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(vertical = 0.dp)
                )
            }
        }
    }
}


@Composable
fun CategoryBottomSheetContent(
    filteredCategory: List<Category>,
    isCategorySheetSelected: () -> Unit,
    onEvent: (TransactionFormEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp, max = 300.dp),
        columns = GridCells.Adaptive(150.dp),
        verticalArrangement = Arrangement.Top,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
//        item(span = { GridItemSpan(maxLineSpan) }) {
//            CategoryBottomSheetHeaderItem()
//        }
        items(filteredCategory, key = { it.id }) { category ->
            CategoryBottomSheetListItem(category = category, isCategorySheetSelected = isCategorySheetSelected, onEvent = onEvent)
        }
    }
}

@Composable
fun CategoryBottomSheetHeaderItem(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Category", style = MaterialTheme.typography.titleLarge)
    }
}
@Composable
fun CategoryBottomSheetListItem(
    category: Category,
    isCategorySheetSelected: () -> Unit,
    onEvent: (TransactionFormEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(8.dp)
            .clickable {
                onEvent(TransactionFormEvent.SetCategory(category))
                isCategorySheetSelected()
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(1.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = category.name, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


/**
 * A composable for selecting transaction type using Material3 FilterChips.
 */
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
                    Log.d("TransactionTypeSelector", "onTypeSelected: $selectedType")
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
                    Log.d("TransactionTypeSelector", "onTypeSelected: $selectedType")
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


/* ============ PREVIEW ============ */

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme (CREATE)")
@Composable
fun TransactionFormCreateScreenLightPreview() {
    SmartFinanceTheme(darkTheme = false) {
        // Provide sample data for preview.
        TransactionFormScreen(
            transactionsFormUiState = TransactionFormUiState.Success(),
            onEvent = { },
            navigateToPrevious = { },
            onTransactionSaved = { }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme (CREATE)")
@Composable
fun TransactionFormCreateScreenDarkPreview() {
    SmartFinanceTheme(darkTheme = false) {
        // Provide sample data for preview.
        TransactionFormScreen(
            transactionsFormUiState = TransactionFormUiState.Success(),
            onEvent = { },
            navigateToPrevious = { },
            onTransactionSaved = { }
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme (EDIT)")
@Composable
fun TransactionFormEditScreenLightPreview() {
    SmartFinanceTheme(darkTheme = false) {
        // Provide sample data for preview.
        val transactionsFormUiState = TransactionFormUiState.Success(
            formState = FormState(
                transactionForm = TransactionForm(
                    category = Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
                    amount = 100.toDouble().toString(),
                    note = "Sample Note"
                )
            )
        )
        // this comment is a test TODO()
        TransactionFormScreen(
            transactionsFormUiState = transactionsFormUiState,
            onEvent = { },
            navigateToPrevious = { },
            onTransactionSaved = { }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme (EDIT)")
@Composable
fun TransactionFormEditScreenDarkPreview() {
    SmartFinanceTheme(darkTheme = true) {
        // Provide sample data for preview.
        val transactionsFormUiState = TransactionFormUiState.Success(
            formState = FormState(
                transactionForm = TransactionForm(
                    category = Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
                    amount = 100.toDouble().toString(),
                    note = "Sample Note"
                )
            )
        )
        // this comment is a test TODO()
        TransactionFormScreen(
            transactionsFormUiState = transactionsFormUiState,
            onEvent = { },
            navigateToPrevious = { },
            onTransactionSaved = { }
        )
    }
}

