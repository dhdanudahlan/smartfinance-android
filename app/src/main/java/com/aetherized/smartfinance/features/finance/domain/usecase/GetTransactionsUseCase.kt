package com.aetherized.smartfinance.features.finance.domain.usecase

import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTransactionsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    operator fun invoke (
        categoryId: Long? = null,
        categoryType: CategoryType? = null,
        limit: Int = 50,
        offset: Int = 0
    ): Flow<List<Transaction>> {
        return when {
            categoryId != null -> repository.getTransactionsByCategory(categoryId, limit, offset)
            categoryType != null -> repository.getTransactionsByType(categoryType, limit, offset)
            else -> repository.getActiveTransactions(limit, offset)
        }
    }
}
