package com.aetherized.smartfinance.features.finance.presentation.screen

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.presentation.TransactionFormUiState
import com.aetherized.smartfinance.ui.theme.SmartFinanceTheme


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme (CREATE)")
@Composable
fun TransactionFormCreateScreenLightPreview() {
    SmartFinanceTheme(darkTheme = false) {
        // Provide sample data for preview.
        val formState = TransactionFormUiState.FormState(
            category = Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
            amount = 100.toDouble(),
            note = "Sample Note",
        )
        TransactionFormScreen(
            formState = formState,
            onCategoryTypeChanged = { },
            onCategoryChanged = { },
            onDateTimeChanged = { },
            onAmountChanged = { },
            onNoteChanged = { },
            onSaveClicked = { },
            onCopyClicked = { },
            onCancelClicked = { },
            onContinueClicked = { },
            onDeleteClicked = { },
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme (CREATE)")
@Composable
fun TransactionFormCreateScreenDarkPreview() {
    SmartFinanceTheme(darkTheme = true) {
        // Provide sample data for preview.
        val formState = TransactionFormUiState.FormState(
            category = Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
            amount = 100.toDouble(),
            note = "Sample Note",
        )
        TransactionFormScreen(
            formState = formState,
            onCategoryTypeChanged = { },
            onCategoryChanged = { },
            onDateTimeChanged = { },
            onAmountChanged = { },
            onNoteChanged = { },
            onSaveClicked = { },
            onCopyClicked = { },
            onCancelClicked = { },
            onContinueClicked = { },
            onDeleteClicked = { },
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Theme (EDIT)")
@Composable
fun TransactionFormEditScreenLightPreview() {
    SmartFinanceTheme(darkTheme = false) {
        // Provide sample data for preview.
        val formState = TransactionFormUiState.FormState(
            category = Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
            amount = 100.toDouble(),
            note = "Sample Note",
        )
        TransactionFormScreen(
            formState = formState,
            onCategoryTypeChanged = { },
            onCategoryChanged = { },
            onDateTimeChanged = { },
            onAmountChanged = { },
            onNoteChanged = { },
            onSaveClicked = { },
            onCopyClicked = { },
            onCancelClicked = { },
            onContinueClicked = { },
            onDeleteClicked = { },
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Theme (EDIT)")
@Composable
fun TransactionFormEditScreenDarkPreview() {
    SmartFinanceTheme(darkTheme = true) {
        // Provide sample data for preview.
        val formState = TransactionFormUiState.FormState(
            category = Category(id = 1, name = "Category Expense 1", type = CategoryType.EXPENSE),
            amount = 100.toDouble(),
            note = "Sample Note",
        )
        TransactionFormScreen(
            formState = formState,
            onCategoryTypeChanged = { },
            onCategoryChanged = { },
            onDateTimeChanged = { },
            onAmountChanged = { },
            onNoteChanged = { },
            onSaveClicked = { },
            onCopyClicked = { },
            onCancelClicked = { },
            onContinueClicked = { },
            onDeleteClicked = { },
        )
    }
}

