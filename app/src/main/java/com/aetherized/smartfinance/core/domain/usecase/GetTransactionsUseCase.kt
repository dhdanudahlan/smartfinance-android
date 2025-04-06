package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.model.data.CategoryType
import com.aetherized.smartfinance.core.model.data.Transaction
import com.aetherized.smartfinance.core.data.repository.FinanceRepository
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
