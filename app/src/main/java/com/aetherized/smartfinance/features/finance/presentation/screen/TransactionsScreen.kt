package com.aetherized.smartfinance.features.finance.presentation.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.core.utils.toCommaSeparatedString
import com.aetherized.smartfinance.core.utils.toRupiahString
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.MonthlySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.presentation.FilterState
import com.aetherized.smartfinance.features.finance.presentation.TransactionsEvent
import com.aetherized.smartfinance.features.finance.presentation.TransactionsTab
import com.aetherized.smartfinance.features.finance.presentation.TransactionsUiState
import com.aetherized.smartfinance.features.finance.presentation.TransactionsViewModel
import com.aetherized.smartfinance.features.finance.presentation.component.getDatesOfTheYearMonth
import com.aetherized.smartfinance.features.finance.presentation.component.titleCase
import com.aetherized.smartfinance.features.finance.presentation.component.toNumericalString
import com.aetherized.smartfinance.ui.theme.SmartFinanceTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


/** -------------------------
Screen of Transactions
----------------------------*/

@Composable
fun TransactionsScreenContainer(
    viewModel: TransactionsViewModel = hiltViewModel(),
    onTransactionClick: (Long) -> Unit,
    onFabClick: () -> Unit,
) {


    val transactionsUiState by viewModel.transactionsUiState.collectAsState()

    TransactionsScreen(
        transactionsUiState = transactionsUiState,
        onEvent = viewModel::onEvent,
        onTransactionClick = onTransactionClick,
        onFabClick = onFabClick
    )
}

@Composable
fun TransactionsScreen(
    transactionsUiState: TransactionsUiState,
    onEvent: (TransactionsEvent) -> Unit,
    onTransactionClick: (Long) -> Unit,
    onFabClick: () -> Unit
) {
    val selectedTab = transactionsUiState.filterState.selectedTab

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmartFinanceTopAppBar(
                yearMonth = transactionsUiState.filterState.yearMonth,
                selectedTab = selectedTab,
                onPreviousYearMonth = { onEvent(if (selectedTab == TransactionsTab.Daily) TransactionsEvent.NavigateToPreviousMonth else TransactionsEvent.NavigateToPreviousYear) },
                onNextYearMonth = { onEvent(if (selectedTab == TransactionsTab.Daily) TransactionsEvent.NavigateToNextMonth else TransactionsEvent.NavigateToNextYear) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onFabClick() }
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
            // Tabs: Daily, Monthly, Total
            TransactionsTabRow(
                selectedTab = selectedTab,
                onTabSelected = { newSelectedTab ->
                    onEvent(TransactionsEvent.ChangeTransactionsTab(newSelectedTab))
                }
            )
            when (transactionsUiState) {
                is TransactionsUiState.Loading -> {
                    // Pass all necessary data to the detailed screen.
                    TransactionsSummaryRow(
                        incomes = 0.0,
                        expenses = 0.0
                    )
//                  LoadingScreen("Loading transactions")
                }
                is TransactionsUiState.Success -> {
                    // Pass all necessary data to the detailed screen.
                    Log.d("TransactionsScreen","TransactionsUIState.Success ${transactionsUiState.transactions.firstOrNull()}")


                    when(selectedTab) {
                        is TransactionsTab.Daily -> {
                            Log.d("TransactionScreen", "TransactionsScreen selectedTab: Daily")

                            // Render a summary row (using precomputed summaries if desired).
                            TransactionsSummaryRow(
                                incomes = transactionsUiState.dailySummaries.sumOf { it.income },
                                expenses = transactionsUiState.dailySummaries.sumOf { it.expense }
                            )

                            TransactionsTabContent(
                                dailySummaries = transactionsUiState.dailySummaries,
                                yearMonth = transactionsUiState.filterState.yearMonth,
                                categories = transactionsUiState.categories,
                                onTransactionClick = onTransactionClick
                            )
                        }
                        is TransactionsTab.Monthly -> {
                            Log.d("TransactionScreen", "TransactionsScreen selectedTabIndex: ELSE")

                            // Render a summary row (using precomputed summaries if desired).
                            TransactionsSummaryRow(
                                incomes = transactionsUiState.monthlySummaries.sumOf { it.income },
                                expenses = transactionsUiState.monthlySummaries.sumOf { it.expense }
                            )

                            TransactionsTabMonthlyContent(
                                monthlySummaries = transactionsUiState.monthlySummaries,
                                yearMonth = transactionsUiState.filterState.yearMonth,
                                categories = transactionsUiState.categories,
                                onTransactionClick = onTransactionClick
                            )
                        }
                        is TransactionsTab.Total -> {
                            Log.d("TransactionScreen", "TransactionsScreen selectedTabIndex: ELSE")
                        }
                    }
                }
                is TransactionsUiState.Error -> {
                    //            Log.d("TransactionsScreen","TransactionsUIState.Error ${errorState.message}")
                    // Display an error message with a retry button
                    // Pass all necessary data to the detailed screen.
                    TransactionsSummaryRow(
                        incomes = 0.0,
                        expenses = 0.0
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading transactions:\n${transactionsUiState.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { onEvent(TransactionsEvent.FetchTransactions()) }) { // Retry button
                            Text("Retry")
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun TransactionsTabContent(
    dailySummaries: List<DailySummary>,
    yearMonth: YearMonth,
    categories: List<Category>,
    onTransactionClick: (Long) -> Unit
) {
    val previousMonth by remember { mutableStateOf(yearMonth) }
    // Determine slide direction
    val slideDirection = if (yearMonth > previousMonth) AnimatedContentTransitionScope.SlideDirection.Left
    else AnimatedContentTransitionScope.SlideDirection.Right

    // Render the daily transaction using the precomputed daily summaries.
    AnimatedContent(
        targetState = dailySummaries,
        transitionSpec = {
            slideIntoContainer(slideDirection, animationSpec = tween(300)) togetherWith
                slideOutOfContainer(slideDirection, animationSpec = tween(300))
        }
    ) { dailySummaryList ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.height(2.dp)) }

            items(dailySummaryList) { dailySummary ->
                SmartFinanceDailyTransactionsCard(
                    dailySummary = dailySummary,
                    categories = categories,
                    onTransactionClick = onTransactionClick
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
fun TransactionsTabMonthlyContent(
    monthlySummaries: List<MonthlySummary>,
    yearMonth: YearMonth,
    categories: List<Category>,
    onTransactionClick: (Long) -> Unit
) {
    val previousYear by remember { mutableStateOf(yearMonth) }
    // Determine slide direction
    val slideDirection = if (yearMonth > previousYear) AnimatedContentTransitionScope.SlideDirection.Left
    else AnimatedContentTransitionScope.SlideDirection.Right

    // Render the daily transaction using the precomputed daily summaries.
    AnimatedContent(
        targetState = monthlySummaries,
        transitionSpec = {
            slideIntoContainer(slideDirection, animationSpec = tween(300)) togetherWith
                slideOutOfContainer(slideDirection, animationSpec = tween(300))
        }
    ) { monthlySummaryList ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.height(2.dp)) }

            items(monthlySummaryList) { monthlySummary ->
                SmartFinanceMonthlyTransactionsCard(
                    monthlySummary = monthlySummary,
                    categories = categories,
                    onTransactionClick = onTransactionClick
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
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
    selectedTab: TransactionsTab = TransactionsTab.Daily,
    onPreviousYearMonth: () -> Unit,
    onNextYearMonth: () -> Unit
) {


    val yearMonthFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    // Compute the formatted month only when selectedMonth changes
    val formattedYearMonth by remember(yearMonth) {
        derivedStateOf { yearMonth.format(yearMonthFormatter) }
    }
    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = onPreviousYearMonth) {
                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Year Month")
            }
            Text(
                text = if (selectedTab == TransactionsTab.Daily) { formattedYearMonth } else { yearMonth.year.toString() },
                maxLines = 1,
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(horizontal = 2.dp),
                textAlign = TextAlign.Center,
            )
            IconButton(onClick = onNextYearMonth) {
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Year Month")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { /* search action */ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        },
    )

}


/** -------------------------
Tab Row: Daily, Monthly, Total
----------------------------*/
@Composable
fun TransactionsTabRow(
    modifier: Modifier = Modifier,
    selectedTab: TransactionsTab,
    onTabSelected: (TransactionsTab) -> Unit,
) {
    // Create a list of tabs you want to display.
    val tabs = listOf(TransactionsTab.Daily, TransactionsTab.Monthly)
    // Compute the selected index from the current tab.
    val selectedIndex = tabs.indexOf(selectedTab)

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = {
                    Text(
                        text = tab.displayName,
                        maxLines = 1,
                        fontSize = 11.sp
                    )
                },
                selected = selectedIndex == index,
                onClick = { onTabSelected(tab) },
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
    incomes: Double,
    expenses: Double
) {
    val total = incomes - expenses
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        SummaryRowItem(label = "Income", value = incomes, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
        SummaryRowItem(label = "Expenses", value = expenses, color = Color.Red, modifier = Modifier.weight(1f))
        SummaryRowItem(label = "Total", value = total, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.weight(1f))
    }
}

@Composable
fun SummaryRowItem(modifier: Modifier = Modifier, label: String, value: Double, color: Color) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Text(
            text = value.toCommaSeparatedString(),
            fontSize = 14.sp,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/** -------------------------
SmartFinance Transactions Card
-------------------------- **/

@Composable
fun SmartFinanceTransactionsCardCommon(
    headerContent: @Composable () -> Unit,
    transactions: List<Transaction>,
    categories: List<Category>,
    onTransactionClick: (Long) -> Unit
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
            // Render the header (daily or monthly)
            headerContent()
            // Render each transaction
            transactions.forEach { transaction ->
                val category = categories.find { it.id == transaction.categoryId }
                category?.let {
                    SmartFinanceTransactionsItem(
                        transaction = transaction,
                        category = it,
                        onTransactionClick = onTransactionClick
                    )
                }
            }
        }
    }
}

/** ----- Daily Transactions Card ----- **/

@Composable
fun SmartFinanceDailyTransactionsCard(
    dailySummary: DailySummary,
    categories: List<Category>,
    onTransactionClick: (Long) -> Unit
) {

    SmartFinanceTransactionsCardCommon(
        headerContent = {
            SmartFinanceDailyTransactionsHeader(
                incomes = dailySummary.income,
                expenses = dailySummary.expense,
                localDate = dailySummary.date
            )
        },
        transactions = dailySummary.transactions,
        categories = categories,
        onTransactionClick = onTransactionClick
    )
}


/** ----- Monthly Transactions Card ----- **/

@Composable
fun SmartFinanceMonthlyTransactionsCard(
    monthlySummary: MonthlySummary,
    categories: List<Category>,
    onTransactionClick: (Long) -> Unit
) {
    SmartFinanceTransactionsCardCommon(
        headerContent = {
            SmartFinanceMonthlyTransactionsHeader(
                incomes = monthlySummary.income,
                expenses = monthlySummary.expense,
                yearMonth = monthlySummary.yearMonth
            )
        },
        transactions = monthlySummary.transactions,
        categories = categories,
        onTransactionClick = onTransactionClick
    )
}



/** -------------------------
SmartFinance Transactions Header
-------------------------- **/
@Composable
fun SmartFinanceTransactionsHeaderCommon(
    headerContent: @Composable () -> Unit,
    incomes: Double,
    expenses: Double,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // Render the header (daily or monthly)
        headerContent()

        // Render information
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


/** ----- Daily Transaction Header ----- **/
@Composable
fun SmartFinanceDailyTransactionsHeader(
    incomes: Double,
    expenses: Double,
    localDate: LocalDate
) {
    SmartFinanceTransactionsHeaderCommon(
        headerContent = {
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
        },
        incomes = incomes,
        expenses = expenses
    )
}


/** ----- Monthly Transaction Header ----- **/
@Composable
fun SmartFinanceMonthlyTransactionsHeader(
    incomes: Double,
    expenses: Double,
    yearMonth: YearMonth
) {
    SmartFinanceTransactionsHeaderCommon(
        headerContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = yearMonth.month.name.slice(0..2).titleCase(),
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
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
        },
        incomes = incomes,
        expenses = expenses
    )
}


@Composable
fun SmartFinanceTransactionsItem(
    transaction: Transaction,
    category: Category,
    onTransactionClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onTransactionClick(transaction.id)
                Log.d("TransactionsScreen", "onTransactionClick: ${transaction.id} | ${transaction.amount}")
            }
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



/* ============ PREVIEW ============ */

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun TransactionsScreenPreview() {
    val categories = listOf(
        Category(
            id = 0,
            name = "Food",
            type = CategoryType.EXPENSE,
        ),
        Category(
            id = 1,
            name = "Salary",
            type = CategoryType.INCOME,
        )
    )

    val transactions = listOf(
        Transaction(
            categoryId = categories[0].id,
            assetId = 1,
            amount = 60000.toDouble(),
            note = "Transaction of ${LocalDateTime.now().plusMinutes(1).minute}",
            timestamp = LocalDateTime.now().plusMinutes(1)
        ),
        Transaction(
            categoryId = categories[1].id,
            assetId = 1,
            amount = 1000.toDouble(),
            timestamp = LocalDateTime.now().plusMinutes(2)
        ),
        Transaction(
            categoryId = categories[1].id,
            assetId = 1,
            amount = 5000.toDouble(),
            note = "Transaction of ${LocalDateTime.now().plusMinutes(3).minute}",
            timestamp = LocalDateTime.now().plusMinutes(3)
        ),
        Transaction(
            categoryId = categories[0].id,
            assetId = 1,
            amount = 1000.toDouble(),
            note = "Transaction of ${LocalDateTime.now().minusDays(2).plusMinutes(3).minute}",
            timestamp = LocalDateTime.now().minusDays(2).plusMinutes(3)
        ),
        Transaction(
            categoryId = categories[1].id,
            assetId = 1,
            amount = 60000.toDouble(),
            timestamp = LocalDateTime.now().minusDays(2).plusMinutes(2)
        ),
        Transaction(
            categoryId = categories[1].id,
            assetId = 1,
            amount = 5000.toDouble(),
            note = "Transaction of ${LocalDateTime.now().minusDays(2).plusMinutes(1).minute}",
            timestamp = LocalDateTime.now().minusDays(2).plusMinutes(1)
        )
    )

    val daysInMonth = getDatesOfTheYearMonth()

//    Group transactions by date.
    val transactionsByDate = transactions.groupBy { it.timestamp.toLocalDate() }

    val dailySummaries = daysInMonth.mapNotNull { date ->
        val dailyTransactions = transactionsByDate[date].orEmpty()
        // Only include days that have transactions.
        if (dailyTransactions.isNotEmpty()) {
            // Get income and expense category IDs.
            val incomeIds = categories.filter { it.type == CategoryType.INCOME }.map { it.id }.toSet()
            val expenseIds = categories.filter { it.type == CategoryType.EXPENSE }.map { it.id }.toSet()

            val incomeTotal = dailyTransactions.filter { it.categoryId in incomeIds }.sumOf { it.amount }
            val expenseTotal = dailyTransactions.filter { it.categoryId in expenseIds }.sumOf { it.amount }

            DailySummary(
                date = date,
                transactions = dailyTransactions,
                income = incomeTotal,
                expense = expenseTotal
            )
        } else {
            null
        }
    }
    val successTransactionsState = TransactionsUiState.Success(
        transactions = transactions,
        dailySummaries = dailySummaries,
        filterState = FilterState(
            yearMonth = YearMonth.now()
        )
    )
    SmartFinanceTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TransactionsScreen(
                transactionsUiState = successTransactionsState,
                onEvent = { },
                onTransactionClick = { },
                onFabClick = { }
            )
        }
    }
}

@Preview
@Composable
private fun SmartFinanceTransactionsCardPreview() {
    val categories = listOf(
        Category(
            id = 0,
            name = "Food",
            type = CategoryType.EXPENSE,
        ),
        Category(
            id = 1,
            name = "Salary",
            type = CategoryType.INCOME,
        )
    )
    val transactions = listOf(
        Transaction(
            categoryId = categories[0].id,
            assetId = 1,
            amount = 6000000.toDouble(),
            note = "Transaction of 6000000"
        ),
        Transaction(
            categoryId = categories[1].id,
            assetId = 1,
            amount = 1000.toDouble(),
        ),
        Transaction(
            categoryId = categories[1].id,
            assetId = 1,
            amount = 5000.toDouble(),
            note = "Transaction of 5000"
        )
    )
    val localDateTime = LocalDateTime.now()

    // Get income and expense category IDs.
    val incomeIds = categories.filter { it.type == CategoryType.INCOME }.map { it.id }.toSet()
    val expenseIds = categories.filter { it.type == CategoryType.EXPENSE }.map { it.id }.toSet()

    val incomeTotal = transactions.filter { it.categoryId in incomeIds }.sumOf { it.amount }
    val expenseTotal = transactions.filter { it.categoryId in expenseIds }.sumOf { it.amount }

    val dailySummary = DailySummary(
        date = LocalDate.of(localDateTime.year, localDateTime.month, localDateTime.dayOfMonth),
        transactions = transactions,
        income = incomeTotal,
        expense = expenseTotal
    )
    SmartFinanceDailyTransactionsCard(
        dailySummary = dailySummary,
        categories = categories,
        onTransactionClick = { }
    )
}
