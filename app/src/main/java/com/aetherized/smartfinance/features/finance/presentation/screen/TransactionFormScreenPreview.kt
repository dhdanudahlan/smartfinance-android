package com.aetherized.smartfinance.features.finance.presentation.screen

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.ui.theme.SmartFinanceTheme


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme (CREATE)")
@Composable
fun TransactionFormCreateScreenLightPreview() {
    SmartFinanceTheme(darkTheme = false) {
        // Provide sample data for preview.
        TransactionFormScreen(
            transaction = null,
            allCategories = listOf(
                Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
                Category(id = 2, name = "Category Income 1", type = CategoryType.INCOME)
            ),
            onSave = { _, _ -> },
            onDelete = { },
            onCopy = { },
            onCancel = { },
            onPickDateTime = { current, onDateTimeSelected -> onDateTimeSelected(current) }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme (CREATE)")
@Composable
fun TransactionFormCreateScreenDarkPreview() {
    SmartFinanceTheme(darkTheme = true) {
        // Provide sample data for preview.
        TransactionFormScreen(
            transaction = null,
            allCategories = listOf(
                Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
                Category(id = 2, name = "Category Income 1", type = CategoryType.INCOME)
            ),
            onSave = { _, _ -> },
            onDelete = { },
            onCopy = { },
            onCancel = { },
            onPickDateTime = { current, onDateTimeSelected -> onDateTimeSelected(current) }
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme (EDIT)")
@Composable
fun TransactionFormEditScreenLightPreview() {
    SmartFinanceTheme(darkTheme = false) {
        // Provide sample data for preview.
        TransactionFormScreen(
            transaction = Transaction(
                id = 1,
                categoryId = 1,
                amount = 100,
                note = "Sample Note",
            ),
            allCategories = listOf(
                Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
                Category(id = 2, name = "Category Income 1", type = CategoryType.INCOME)
            ),
            onSave = { _, _ -> },
            onDelete = { },
            onCopy = { },
            onCancel = { },
            onPickDateTime = { current, onDateTimeSelected -> onDateTimeSelected(current) }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme (EDIT)")
@Composable
fun TransactionFormEditScreenDarkPreview() {
    SmartFinanceTheme(darkTheme = true) {
        // Provide sample data for preview.
        TransactionFormScreen(
            transaction = Transaction(
                id = 1,
                categoryId = 1,
                amount = 100,
                note = "Sample Note",
            ),
            allCategories = listOf(
                Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
                Category(id = 2, name = "Category Income 1", type = CategoryType.INCOME)
            ),
            onSave = { _, _ -> },
            onDelete = { },
            onCopy = { },
            onCancel = { },
            onPickDateTime = { current, onDateTimeSelected -> onDateTimeSelected(current) }
        )
    }
}

