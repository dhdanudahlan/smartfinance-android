package com.aetherized.smartfinance.features.finance.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteCategoryUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val upsertCategoryUseCase: UpsertCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CategoriesUIState>(CategoriesUIState.Loading)
    val uiState: StateFlow<CategoriesUIState> = _uiState

    private var selectedType: CategoryType? = null

    init {
        Log.d("UI", "CategoryViewModel init")
        loadCategories(selectedType)
    }

    private fun loadCategories(type: CategoryType? = null, limit: Int = 50, offset: Int = 0) {
        selectedType = type
        _uiState.value = CategoriesUIState.Loading

        viewModelScope.launch {
            try {
                getCategoriesUseCase(
                    categoryType = type,
                    limit = limit,
                    offset = offset
                ).collect { categories ->
                    _uiState.value = CategoriesUIState.Success(
                        categories = categories,
                        selectedType = type
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CategoriesUIState.Error(
                    message = e.message ?: "Unknown Error"
                )
            }
        }
    }

    fun createOrUpdateCategory(id: Long?, name: String, type: CategoryType) {
        viewModelScope.launch {
            try {
                val newCategory = Category(
                    id = id ?: -1,
                    name = name,
                    type = type
                )
                upsertCategoryUseCase(newCategory)
            } catch (e: Exception) {
                _uiState.value = CategoriesUIState.Error(e.message ?: "Unable to upsert category")
            }
        }
    }

    fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            try {
                deleteCategoryUseCase(categoryId)
            } catch (e: Exception) {
                _uiState.value = CategoriesUIState.Error(e.message ?: "Unable to delete category")
            }
        }
    }
}
