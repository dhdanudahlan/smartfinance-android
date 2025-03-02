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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.core.utils.toCommaSeparatedString
import com.aetherized.smartfinance.core.utils.toRupiahString
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.presentation.TransactionsUIState
import com.aetherized.smartfinance.features.finance.presentation.TransactionsViewModel
import com.aetherized.smartfinance.features.finance.presentation.component.capitalize
import com.aetherized.smartfinance.features.finance.presentation.component.toNumericalString
import com.aetherized.smartfinance.ui.component.LoadingScreen
import java.time.LocalDate
import java.time.YearMonth


/** -------------------------
Screen of Transactions
----------------------------*/

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TransactionsScreenContainer(
    viewModel: TransactionsViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()


    when (uiState) {
        is TransactionsUIState.Loading -> {
            LoadingScreen("Loading transactions")
        }
        is TransactionsUIState.Success -> {
            // Pass all necessary data to the detailed screen.
            val successState = uiState as TransactionsUIState.Success
            TransactionsScreen(
                transactions = successState.transactions,
                categories = successState.categories,
                yearMonth = successState.yearMonth,
                selectedCategory = successState.selectedCategory,
                selectedCategoryType = successState.selectedCategoryType,
                dailySummaries = successState.dailySummaries,
                onCategorySelected = { category -> viewModel.onCategorySelected(category) },
                onCategoryTypeSelected = { type -> viewModel.onCategoryTypeSelected(type) },
                onPreviousMonth = viewModel::previousMonth,
                onNextMonth = viewModel::nextMonth,
                onNavigateTransaction = { transactionId ->
                    onNavigate("transaction_form?transactionId=${transactionId}")
                }
            )
        }
        is TransactionsUIState.Error -> {
            val errorState = uiState as TransactionsUIState.Error
            Log.d("TransactionsUIState.Error.Screen", errorState.message)
            // Display an error message on-screen.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as TransactionsUIState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun TransactionsScreen(
    transactions: List<Transaction>,
    categories: List<Category>,
    yearMonth: YearMonth,
    selectedCategory: Category?,
    selectedCategoryType: CategoryType,
    dailySummaries: List<DailySummary>,
    onCategorySelected: (Category?) -> Unit,
    onCategoryTypeSelected: (CategoryType) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onNavigateTransaction: (Long) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier,
        topBar = {
            SmartFinanceTopAppBar(
                yearMonth = yearMonth,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateTransaction(-1) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .padding(innerPadding)
        ) {

            // Tabs: Daily, Calendar, Monthly, Total, Note
            TransactionsTabRow(
                modifier = Modifier
                    .fillMaxWidth(),
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> selectedTabIndex = index }
            )
            when(selectedTabIndex) {
                0 -> {
                    TransactionsTabDailyContent(
                        dailySummaries = dailySummaries,
                        categories = categories,
                        onNavigateTransaction = onNavigateTransaction
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun TransactionsTabDailyContent(
    dailySummaries: List<DailySummary>,
    categories: List<Category>,
    onNavigateTransaction: (Long) -> Unit
) {
    // Render a summary row (using precomputed summaries if desired).
    TransactionsSummaryRow(
        incomes = dailySummaries.sumOf { it.income },
        expenses = dailySummaries.sumOf { it.expense }
    )
    // Render the daily transaction using the precomputed daily summaries.
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.height(2.dp)) }
        items(dailySummaries) { dailySummary ->
            SmartFinanceTransactionsCard(
                dailySummary = dailySummary,
                categories = categories,
                onNavigateTransaction = onNavigateTransaction
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

/** -------------------------
Top App Bar (Date + Icons)
----------------------------*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartFinanceTopAppBar(
    yearMonth: YearMonth = YearMonth.now(),
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = onPreviousMonth) {
                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Month")
            }
            Text(
                text = "${yearMonth.month.name.slice(0..2).capitalize()} ${yearMonth.year}",
                maxLines = 1,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(horizontal = 2.dp),
                textAlign = TextAlign.Center,
            )
            IconButton(onClick = onNextMonth) {
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { /* search action */ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { /* filter or settings action */ }) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
            }
        },
    )
}


/** -------------------------
Tab Row: Daily, Calendar, Monthly, Total, Note
----------------------------*/
@Composable
fun TransactionsTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    // State in a real scenario for which tab is selected
//    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Daily", "Calendar", "Monthly", "Total")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                },
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                unselectedContentColor = Color.Gray
            )
        }
    }
}

/** -------------------------
Summary Row: Income, Expenses, Total
----------------------------*/
@Composable
fun TransactionsSummaryRow(
    incomes: Int,
    expenses: Int
) {
    val total = incomes - expenses
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SummaryRowItem(label = "Income", value = incomes, color = MaterialTheme.colorScheme.primary)
        SummaryRowItem(label = "Expenses", value = expenses, color = Color.Red)
        SummaryRowItem(label = "Total", value = total, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun SummaryRowItem(label: String, value: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
        Text(text = value.toCommaSeparatedString(), fontSize = 14.sp, color = color)
    }
}


/** -------------------------
Daily Transaction Rows
----------------------------*/

@Composable
fun SmartFinanceTransactionsCard(
    dailySummary: DailySummary,
    categories: List<Category>,
    onNavigateTransaction: (Long) -> Unit
) {

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            SmartFinanceTransactionsHeader(
                incomes = dailySummary.income,
                expenses = dailySummary.expense,
                localDate = dailySummary.date
            )
            dailySummary.transactions.forEach { transaction ->
                val category = categories.find { it.id == transaction.categoryId }
                if (category != null) {
                    SmartFinanceTransactionsItem(transaction, category, onNavigateTransaction)
                }
            }
        }
    }
}

@Composable
fun SmartFinanceTransactionsHeader(
    incomes: Int,
    expenses: Int,
    localDate: LocalDate
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Row {
            Text(
                text = localDate.dayOfMonth.toNumericalString(),
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = ".${localDate.monthValue.toNumericalString()}.${localDate.year}",
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                fontSize = 11.sp,
            )

            Spacer(modifier = Modifier.width(2.dp))
            Card{
                Text(
                    text = localDate.dayOfWeek.name,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = incomes.toRupiahString(),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = expenses.toRupiahString(),
            color = Color.Red,
            maxLines = 1,
            fontSize = 11.sp
        )
    }
}

@Composable
fun SmartFinanceTransactionsItem(
    transaction: Transaction,
    category: Category,
    onNavigateTransaction: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onNavigateTransaction(transaction.id) }
    ) {
        // Each date row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                color = Color.Gray,
                maxLines = 1,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(80.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (transaction.note != null) {
                    Text(
                        text = transaction.note,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category.type.toString(),
                    color = Color.Gray,
                    maxLines = 1,
                    fontSize = 11.sp
                )
            }
            Row {
                if (category.type == CategoryType.INCOME) {
                    Text(
                        text = "Rp ${transaction.amount.toInt()}",
                        modifier = Modifier.width(80.dp),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                } else {
                    Text(
                        text = "Rp ${transaction.amount.toInt()}",
                        modifier = Modifier.width(80.dp),
                        textAlign = TextAlign.End,
                        color = Color.Red,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryTypePicker(selectedType: CategoryType, onSelected: (CategoryType) -> Unit) {
    Row {
        Button(onClick = { onSelected(CategoryType.INCOME) }) {
            Text("Income")
        }
        Button(onClick = { onSelected(CategoryType.EXPENSE) }) {
            Text("Expense")
        }
        Text("Selected: $selectedType")
    }
}

@Composable
fun CategoryFilterDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = selectedCategory?.name ?: "All"

    Box {
        Text(
            text = "Category: $selectedName",
            modifier = Modifier
                .padding(8.dp)
                .clickable { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                onClick = {
                    onCategorySelected(null)
                    expanded = false
                },
                text = {
                    Text("All")
                }
            )
            categories.forEach { cat ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(cat)
                        expanded = false
                    },
                    text = {
                        Text(cat.name)
                    }
                )
            }
        }
    }

}


