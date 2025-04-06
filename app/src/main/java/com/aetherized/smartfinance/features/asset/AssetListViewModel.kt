package com.aetherized.smartfinance.features.asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface AssetListEvent {
    object FetchAssets : AssetListEvent
}

@HiltViewModel
class AssetListViewModel @Inject constructor(
): ViewModel() {

    private val _assetUiState = MutableStateFlow<AssetListUiState>(AssetListUiState.Loading)
    val uiState: StateFlow<AssetListUiState> = _assetUiState.asStateFlow()

    init {
        onEvent(AssetListEvent.FetchAssets)
    }

    fun onEvent(event: AssetListEvent) {
        when (event) {
            AssetListEvent.FetchAssets -> fetchAssets()
        }
    }

    private fun fetchAssets() {
        viewModelScope.launch {

        }
    }
}
