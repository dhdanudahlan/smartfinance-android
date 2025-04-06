package com.aetherized.smartfinance.features.asset

import com.aetherized.smartfinance.core.model.data.Asset

sealed class AssetListUiState {
    object Loading : AssetListUiState()
    data class Success(val assets: List<Asset>) : AssetListUiState()
    data class Error(val message: String) : AssetListUiState()
}
