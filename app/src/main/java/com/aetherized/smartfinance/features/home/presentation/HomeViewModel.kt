package com.aetherized.smartfinance.features.home.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    // Holds our currently selected home category
    private val selectedHomeCategory = MutableStateFlow(HomeCategory.Accounts)

    // Holds the currently available home categories
    private val homeCategories = MutableStateFlow(HomeCategory.entries)

    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(HomeScreenUiState())

    // Holds the view state if the UI is refreshing for new data
    private val refreshing = MutableStateFlow(false)

    val state: StateFlow<HomeScreenUiState>
        get() = _state

    init {
        viewModelScope.launch {

        }
    }

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true

            }
            refreshing.value = false
        }
    }

    fun onHomeAction(action: HomeAction) {
        when (action) {
            is HomeAction.HomeCategorySelected -> onHomeCategorySelected(action.category)
        }
    }

    private fun onHomeCategorySelected(category: HomeCategory) {
        selectedHomeCategory.value = category
    }

}

enum class HomeCategory {
    Accounts,
    Transactions,
    Statistics,
    Budgets,
    Goals,
    More
}

@Immutable
sealed interface HomeAction {
    data class HomeCategorySelected(val category: HomeCategory) : HomeAction
}

@Immutable
data class HomeScreenUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val selectedHomeCategory: HomeCategory = HomeCategory.Accounts,
    val homeCategories: List<HomeCategory> = emptyList(),
)
