package com.aetherized.smartfinance.features.transactions.domain.usecase

import com.aetherized.smartfinance.core.database.relationship.CategoryWithTransactions
import com.aetherized.smartfinance.features.transactions.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryWithTransactionsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(categoryId: Long): Flow<CategoryWithTransactions> {
        return categoryRepository.getCategoryWithTransactions(categoryId)
    }
}
