package com.aetherized.smartfinance.ui.vm

import androidx.lifecycle.ViewModel
import com.aetherized.smartfinance.ui.state.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseViewModel<State : UIState> : ViewModel() {

    protected val _uiState: MutableStateFlow<State> = MutableStateFlow(initUIState())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    abstract fun initUIState(): State
}
