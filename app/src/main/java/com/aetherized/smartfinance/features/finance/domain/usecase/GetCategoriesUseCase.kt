package com.aetherized.smartfinance.features.finance.domain.usecase

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCategoriesUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    operator fun invoke(
        categoryType: CategoryType? = null,
        limit: Int = 50,
        offset: Int = 0
    ): Flow<List<Category>> {
        return if (categoryType != null) {
            repository.getCategoriesByType(categoryType, limit, offset)
        } else {
            repository.getActiveCategories(limit, offset)
        }
    }
}
