package com.aetherized.smartfinance.features.categories.domain.usecase

import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import com.aetherized.smartfinance.features.categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke (
        categoryType: CategoryType? = null,
        limit: Int = 50,
        offset: Int = 0
    ): Flow<List<Category>> {
        return when {
            categoryType != null -> repository.getCategoriesByType(categoryType, limit, offset)
            else -> repository.getActiveCategories()
        }
    }
}
