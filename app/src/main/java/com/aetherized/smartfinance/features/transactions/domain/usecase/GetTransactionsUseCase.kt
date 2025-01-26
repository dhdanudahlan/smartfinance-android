package com.aetherized.smartfinance.features.transactions.domain.usecase

import com.aetherized.smartfinance.features.categories.data.repository.CategoryRepository
import com.aetherized.smartfinance.features.categories.domain.model.Category
import com.aetherized.smartfinance.features.transactions.data.repository.TransactionRepository
import com.aetherized.smartfinance.features.transactions.domain.model.Transaction
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke (limit: Int = 50, offset: Int = 0): Flow<List<Transaction>> =
        repository.getAllTransactions(limit, offset)
}
