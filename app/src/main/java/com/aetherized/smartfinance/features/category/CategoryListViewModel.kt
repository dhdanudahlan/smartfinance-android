package com.aetherized.smartfinance.features.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherized.smartfinance.core.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.core.model.data.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface CategoryListEvent {
    object FetchAssets : CategoryListEvent
    data class DeleteCategory(val categoryId: Long) : CategoryListEvent
    data class SetCategoryType(val categoryType: CategoryType) : CategoryListEvent
}

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
): ViewModel() {

    private val _categoryUiState = MutableStateFlow<CategoryListUiState>(CategoryListUiState.Loading())
    val uiState: StateFlow<CategoryListUiState> = _categoryUiState.asStateFlow()

    init {
        onEvent(CategoryListEvent.FetchAssets)
    }

    fun onEvent(event: CategoryListEvent) {
        when (event) {
            CategoryListEvent.FetchAssets -> fetchAssets()
            is CategoryListEvent.DeleteCategory -> {}
            is CategoryListEvent.SetCategoryType -> TODO()
        }
    }

    private fun fetchAssets() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .retryWhen { _, attempt ->
                    if (attempt < 5) {
                        delay(1000)
                        true
                    } else {
                        false
                    }
                }
                .catch { e ->
                    _categoryUiState.update { currentState ->
                        CategoryListUiState.Error(
                            message = e.message ?: "Error loading categories",
                        )
                    }
                }
                .collect { categories ->
                    _categoryUiState.update { currentState ->
                        when (currentState) {
                            is CategoryListUiState.Success -> {
                                currentState.copy(categories = categories)
                            }
                            else -> {
                                CategoryListUiState.Loading()
                            }
                        }
                    }
                }
        }
    }

    private fun setCategoryType(type: CategoryType) {
        _categoryUiState.update { currentState ->
            when (currentState) {
                is CategoryListUiState.Success -> {
                    currentState.copy(categoryType = type)
                }

                else -> {
                    CategoryListUiState.Loading(categoryType = type)
                }
            }
        }
    }
}
