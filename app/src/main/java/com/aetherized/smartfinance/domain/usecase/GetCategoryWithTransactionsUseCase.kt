package com.aetherized.smartfinance.domain.usecase

import com.aetherized.smartfinance.data.local.relationship.CategoryWithTransactions
import com.aetherized.smartfinance.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryWithTransactionsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(categoryId: Long): Flow<CategoryWithTransactions> {
        return categoryRepository.getCategoryWithTransactions(categoryId)
    }
}
