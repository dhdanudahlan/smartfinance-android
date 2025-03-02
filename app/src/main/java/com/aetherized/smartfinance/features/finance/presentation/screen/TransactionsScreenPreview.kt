package com.aetherized.smartfinance.features.finance.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.DailySummary
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.presentation.component.getDatesOfTheYearMonth
import com.aetherized.smartfinance.ui.theme.SmartFinanceTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth


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
            amount = 60000,
            note = "Transaction of ${LocalDateTime.now().plusMinutes(1).minute}",
            timestamp = LocalDateTime.now().plusMinutes(1)
        ),
        Transaction(
            categoryId = categories[1].id,
            amount = 1000,
            timestamp = LocalDateTime.now().plusMinutes(2)
        ),
        Transaction(
            categoryId = categories[1].id,
            amount = 5000,
            note = "Transaction of ${LocalDateTime.now().plusMinutes(3).minute}",
            timestamp = LocalDateTime.now().plusMinutes(3)
        ),
        Transaction(
            categoryId = categories[0].id,
            amount = 1000,
            note = "Transaction of ${LocalDateTime.now().minusDays(2).plusMinutes(3).minute}",
            timestamp = LocalDateTime.now().minusDays(2).plusMinutes(3)
        ),
        Transaction(
            categoryId = categories[1].id,
            amount = 60000,
            timestamp = LocalDateTime.now().minusDays(2).plusMinutes(2)
        ),
        Transaction(
            categoryId = categories[1].id,
            amount = 5000,
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
    SmartFinanceTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TransactionsScreen(
                transactions = transactions,
                categories = categories,
                yearMonth = YearMonth.now(),
                selectedCategory = categories[0],
                selectedCategoryType = categories[0].type,
                dailySummaries = dailySummaries,
                onCategorySelected = {  },
                onCategoryTypeSelected = {  },
                onPreviousMonth = {  },
                onNextMonth = {  },
                onNavigateTransaction = {  }
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
            amount = 6000000,
            note = "Transaction of 6000000"
        ),
        Transaction(
            categoryId = categories[1].id,
            amount = 1000,
        ),
        Transaction(
            categoryId = categories[1].id,
            amount = 5000,
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
    SmartFinanceTransactionsCard(
        dailySummary = dailySummary,
        categories = categories,
        onNavigateTransaction = { }
    )
}
