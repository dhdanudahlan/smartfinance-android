package com.aetherized.smartfinance.features.category.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.features.category.CategoryListEvent
import com.aetherized.smartfinance.features.category.CategoryListUiState
import com.aetherized.smartfinance.features.category.CategoryListViewModel

@Composable
fun CategoryListScreenContainer(
    navigateToPrevious: () -> Unit,
    viewModel: CategoryListViewModel = hiltViewModel()
) {
    val categoryListUiState by viewModel.uiState.collectAsState()
    CategoryListScreen(
        categoryListUiState = categoryListUiState,
        onEvent = viewModel::onEvent
    )
}
@Composable
fun CategoryListScreen(
    categoryListUiState: CategoryListUiState,
    onEvent: (CategoryListEvent) -> Unit
) {

}
