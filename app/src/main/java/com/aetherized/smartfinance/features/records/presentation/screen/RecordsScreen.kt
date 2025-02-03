package com.aetherized.smartfinance.features.records.presentation.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import com.aetherized.smartfinance.features.records.domain.model.Transaction
import com.aetherized.smartfinance.features.records.presentation.RecordsUIState
import com.aetherized.smartfinance.features.records.presentation.RecordsViewModel
import com.aetherized.smartfinance.ui.component.LoadingScreen
import com.aetherized.smartfinance.ui.theme.SmartFinanceTheme

@Composable
fun RecordsScreen(
    viewModel: RecordsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is RecordsUIState.Loading -> {
            LoadingScreen("Loading records of ${state.selectedCategoryType}")
        }
        is RecordsUIState.Success -> {
            RecordsScreen(
                transactions = state.transactions,
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                selectedCategoryType = state.selectedCategoryType,
                onCategorySelected = { category -> viewModel.onCategorySelected(category) },
                onCategoryTypeSelected = { type -> viewModel.onCategoryTypeSelected(type) }
            )
        }
        is RecordsUIState.Error -> {
            Log.d("RecordsScreen", state.message)
        }
    }
}

@Composable
fun RecordsScreen(
    transactions: List<Transaction>,
    categories: List<Category>,
    selectedCategory: Category?,
    selectedCategoryType: CategoryType,
    onCategorySelected: (Category?) -> Unit,
    onCategoryTypeSelected: (CategoryType) -> Unit
) {
    Column {
        CategoryTypePicker(
            selectedType = selectedCategoryType,
            onSelected = onCategoryTypeSelected
        )
        CategoryFilterDropdown(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        LazyColumn {
            items(transactions.size) { idx ->
                Text(text = "-----------")
                Text(text = transactions[idx].note ?: "-")
                Text(text = categories.find {
                    it.id == transactions[idx].categoryId
                }?.name ?: "--")
                Text(text = categories.find {
                    it.id == transactions[idx].categoryId
                }?.type?.name ?: "---")
                Text("Transaction: ${transactions[idx].categoryId}, Amount: ${transactions[idx].amount}, Date: ${transactions[idx].timestamp}")
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


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Composable
fun RecordsScreenPreview() {
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
            amount = System.currentTimeMillis().toDouble(),
            note = "Transaction of ${System.currentTimeMillis()} -> ${categories[0].id}"
        ),
        Transaction(
            categoryId = categories[0].id,
            amount = System.currentTimeMillis().toDouble() * 2,
            note = "Transaction of ${System.currentTimeMillis() + 1} -> ${categories[0].id}"
        )
    )
    SmartFinanceTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            RecordsScreen(
                transactions = transactions,
                categories = categories,
                selectedCategory = categories[0],
                selectedCategoryType = categories[0].type,
                onCategorySelected = {  },
                onCategoryTypeSelected = {  }
            )
        }
    }
}
