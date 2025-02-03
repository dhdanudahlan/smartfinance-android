package com.aetherized.smartfinance.features.finance.presentation.screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.presentation.RecordsUIState
import com.aetherized.smartfinance.features.finance.presentation.RecordsViewModel
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
    Scaffold(
        topBar = { SmartFinanceTopAppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs: Daily, Calendar, Monthly, Total, Note
            RecordsTabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF303030))
            )
            /*
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
            */
        }
    }
}

/** -------------------------
Top App Bar (Date + Icons)
----------------------------*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartFinanceTopAppBar() {
    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = { /* go to previous month */ }) {
                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous Month")
            }
            Text(
                text = "Jan 2025",
                maxLines = 1,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier,
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { /* go to next month */ }) {
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { /* star/favorite action */ }) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Star")
            }
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
fun RecordsTabRow(
    modifier: Modifier = Modifier
) {
    // You could have a state in a real scenario for which tab is selected
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Daily", "Calendar", "Monthly", "Total", "Note")

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
                        fontSize = 11.sp)
                },
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                unselectedContentColor = Color.Gray
            )
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
