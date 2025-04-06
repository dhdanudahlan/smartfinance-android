package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.model.data.Category
import com.aetherized.smartfinance.core.model.data.CategoryType
import com.aetherized.smartfinance.core.data.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCategoriesUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    operator fun invoke(
        categoryType: CategoryType? = null
    ): Flow<List<Category>> {
        return if (categoryType != null) {
            repository.getCategoriesByType(categoryType)
        } else {
            repository.getActiveCategories()
        }
    }
}
