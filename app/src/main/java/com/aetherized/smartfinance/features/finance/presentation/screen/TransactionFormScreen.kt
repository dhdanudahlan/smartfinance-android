package com.aetherized.smartfinance.features.finance.presentation.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.R
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.presentation.FormState
import com.aetherized.smartfinance.features.finance.presentation.TransactionForm
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormEvent
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormUiState
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormViewModel
import com.aetherized.smartfinance.ui.theme.SmartFinanceTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
    // Screen title and button text.
    val screenTitle = stringResource(id = if(transactionsFormUiState.formState.transactionForm.categoryType == CategoryType.EXPENSE) R.string.expense else R.string.income)

    val filteredCategories by remember { mutableStateOf(transactionsFormUiState.categories) }

    // State for showing the category selection bottom sheet.
    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val bottomSheetContentList = listOf("Category", "Amount")
    var bottomSheetIndex by remember { mutableIntStateOf(0) }

    val scope = rememberCoroutineScope()

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }

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
            when (bottomSheetIndex) {
                0 -> {
                    val filteredCategory = transactionsFormUiState.categories.filter { it.type == transactionsFormUiState.formState.transactionForm.categoryType }
                    CategoryBottomSheetContent(
                        filteredCategory = filteredCategory,
                        isCategorySheetSelected = {
//                            scope.launch {
//                                scaffoldState.bottomSheetState.hide()
//                            }
                            focusRequester2.requestFocus()
                        },
                        onEvent = onEvent
                    )
                }
                1 -> {
                    NumericKeyboardBottomSheetContent(onEvent = onEvent, nextColumn = { focusRequester3.requestFocus() })
                }
                else -> {

                }
            }
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
        sheetPeekHeight = 0.dp,
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {

                    // Transaction Type Selector using FilterChips.
                    TransactionTypeSelector(
                        selectedType = transactionsFormUiState.formState.transactionForm.categoryType,
                        onTypeSelected = { newType ->
                            onEvent(TransactionFormEvent.SetCategoryType(newType))
                            focusRequester1.requestFocus()
                            filteredCategories.filter {
                                it.type == newType
                            }
                            Log.d("TransactionFormScreen", "isEditMode: $transactionsFormUiState.formState.isEditMode")
                        },
                    )
                    // Date & Time Input Field.
                    TransactionFormDateTimeItem(
                        label = "Date",
                        value = transactionsFormUiState.formState.transactionForm.dateTime,
                        onValueChange = { /* Read-only */ },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            },
                        onEvent = onEvent,
                        trailingIcon = {
                            IconButton(onClick = { /* Implement date/time picker */ }) {
                                Icon(Icons.Filled.DateRange, contentDescription = "Date and Time", tint = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    )


                    // Category Selection Field.
                    TransactionFormItem(
                        label = "Category",
                        focusRequester = focusRequester1,
                        value = transactionsFormUiState.formState.transactionForm.category?.name,
                        onValueChange = { /* Read-only */ },
                        onFocused = {
                            bottomSheetIndex = 0
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
                            Icon(Icons.Rounded.Close, contentDescription = "Amount", tint = Color.Transparent)
                        }
                    )


                    // Amount Input Field.
                    TransactionFormItem(
                        label = "Amount",
                        focusRequester = focusRequester2,
                        value = transactionsFormUiState.formState.transactionForm.amount,
                        onValueChange = {
                            onEvent(TransactionFormEvent.SetAmount(it))
                        },
                        onFocused = {
                            bottomSheetIndex = 1
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onUnfocused = {
                            scope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { onEvent(TransactionFormEvent.SetAmount("")) }) {
                                Icon(Icons.Rounded.Close, contentDescription = "Amount")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        readOnly = true
                    )

                    // Note Input Field.
                    TransactionFormItem(
                        label = "Note",
                        focusRequester = focusRequester3,
                        value = transactionsFormUiState.formState.transactionForm.note,
                        onValueChange = {
                            onEvent(TransactionFormEvent.SetNote(it))
                        },
                        placeholder = { Text(text = "Optional", style = MaterialTheme.typography.bodySmall, color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { onEvent(TransactionFormEvent.SetNote("")) }) {
                                Icon(Icons.Rounded.Close, contentDescription = "Amount")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )

                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {
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
                            if (!transactionsFormUiState.formState.isEditMode) {
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormItem(
    label: String,
    focusRequester: FocusRequester,
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
                    }
                    .focusRequester(focusRequester),
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormDateTimeItem(
    label: String,
    modifier: Modifier = Modifier,
    value: LocalDateTime,
    onValueChange: (String) -> Unit,
    onEvent: (TransactionFormEvent) -> Unit,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
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


        val context = LocalContext.current

        // Formatters for display (e.g., 3/28/25 (Fri), 4:13 AM)
        val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy (E)")
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")  // 12-hour format with AM/PM

        // Convert the current LocalDateTime into display strings
        val dateString = remember(value) {
            value.toLocalDate().format(dateFormatter)
        }
        val timeString = remember(value) {
            value.toLocalTime().format(timeFormatter)
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
                value = "",
                onValueChange = onValueChange,
                readOnly = readOnly,
                modifier = modifier
                    .fillMaxWidth(),
                textStyle = textStyle,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                cursorBrush = cursorBrush,
                interactionSource = interactionSource
            ) { _ ->

                TextFieldDefaults.DecorationBox(
                    value = "",
                    visualTransformation = visualTransformation,
                    innerTextField = {
                        Row {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(
                                        onClick = {
                                            // Show the DatePickerDialog when date is clicked
                                            val currentDate = value.toLocalDate()
                                            val datePickerDialog = DatePickerDialog(
                                                context,
                                                { _, year, month, dayOfMonth ->
                                                    // month is zero-based in DatePickerDialog, hence (month + 1)
                                                    onEvent(
                                                        TransactionFormEvent.SetDate(
                                                            LocalDate.of(
                                                                year,
                                                                month + 1,
                                                                dayOfMonth
                                                            )
                                                        )
                                                    )
                                                    //                                        onDateChange(LocalDate.of(year, month + 1, dayOfMonth))
                                                },
                                                currentDate.year,
                                                currentDate.monthValue - 1,
                                                currentDate.dayOfMonth
                                            )
                                            datePickerDialog.show()
                                        },
                                        interactionSource = remember { MutableInteractionSource() }, // This is mandatory
                                        indication = null
                                    )
                            ) {
                                Text(
                                    text = dateString,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .clickable(
                                        onClick = {
                                            // Show the TimePickerDialog when time is clicked
                                            val currentTime = value.toLocalTime()
                                            val timePickerDialog = TimePickerDialog(
                                                context,
                                                { _, hourOfDay, minute ->
                                                    onEvent(
                                                        TransactionFormEvent.SetTime(
                                                            LocalTime.of(
                                                                hourOfDay,
                                                                minute
                                                            )
                                                        )
                                                    )
                                                    //                                        onTimeChange(LocalTime.of(hourOfDay, minute))
                                                },
                                                currentTime.hour,
                                                currentTime.minute,
                                                false // is24HourView = false for 12-hour format
                                            )
                                            timePickerDialog.show()
                                        },
                                        interactionSource = remember { MutableInteractionSource() }, // This is mandatory
                                        indication = null
                                    )
                            ) {
                                Text(
                                    text = timeString,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier,
                                    maxLines = 1
                                )
                            }
                        }
                    },
                    enabled = true,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    trailingIcon = trailingIcon,
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
    onEvent: (TransactionFormEvent) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp, max = 300.dp),
        columns = GridCells.Adaptive(150.dp),
        verticalArrangement = Arrangement.Top,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(filteredCategory, key = { it.id }) { category ->
            CategoryBottomSheetListItem(category = category, isCategorySheetSelected = isCategorySheetSelected, onEvent = onEvent)
        }
    }
}
@Composable
fun CategoryBottomSheetListItem(
    category: Category,
    isCategorySheetSelected: () -> Unit,
    onEvent: (TransactionFormEvent) -> Unit,
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

@Composable
fun NumericKeyboardBottomSheetContent(
    onEvent: (TransactionFormEvent) -> Unit,
    nextColumn: () -> Unit
) {

    val buttons = listOf(
        "7", "8", "9", "<",
        "4", "5", "6", "-",
        "1", "2", "3", "C",
        "", "0", "", "D"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(4.dp), // Reduced padding
        verticalArrangement = Arrangement.spacedBy(4.dp), // Reduced spacing
        horizontalArrangement = Arrangement.spacedBy(4.dp), // Reduced spacing
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 240.dp, max = 240.dp)
    ) {
        items(buttons) { button ->
            when (button) {
                "<" ->
                    NumericKeyboardBottomSheetKeyItem(char = button, onEvent = { onEvent(TransactionFormEvent.EraseAmountChar) }, icon = Icons.AutoMirrored.Rounded.Backspace)
                "-" ->
                    NumericKeyboardBottomSheetKeyItem(char = button, onEvent = { onEvent(TransactionFormEvent.ChangeAmountPosNev) })
                "C" ->
                    NumericKeyboardBottomSheetKeyItem(char = button, onEvent = { onEvent(TransactionFormEvent.SetAmount("")) })
                in "1".."9", "0" ->
                    NumericKeyboardBottomSheetKeyItem(char = button, onEvent = { onEvent(TransactionFormEvent.AddAmountChar(button)) })
                "" ->
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                else ->
                    NumericKeyboardBottomSheetKeyItem(char = button, onEvent = { nextColumn() }, icon = Icons.Rounded.Done)
            }
        }
    }
}
@Composable
fun NumericKeyboardBottomSheetKeyItem(
    char: String,
    icon: ImageVector? = null,
    onEvent: () -> Unit,
) {
    Button(
        onClick = onEvent,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .heightIn(min = 48.dp)
            .fillMaxWidth(),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if(char != "D") MaterialTheme.colorScheme.background else Color.Green, contentColor = MaterialTheme.colorScheme.onBackground)
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null)
        } else {
            Text(text = char, fontSize = 24.sp, fontWeight = FontWeight.Bold)
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

