package com.aetherized.smartfinance.features.asset.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.features.asset.AssetListViewModel

@Composable
fun AssetListScreenContainer(
    navigateToPrevious: () -> Unit,
    viewModel: AssetListViewModel = hiltViewModel()
) {
    val assetListUiState by viewModel.uiState.collectAsState()
}
@Composable
fun AssetListScreen() {

}
