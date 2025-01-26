package com.aetherized.smartfinance.features.transactions.domain.usecase

import com.aetherized.smartfinance.features.categories.data.repository.CategoryRepository
import com.aetherized.smartfinance.features.categories.domain.model.Category
import com.aetherized.smartfinance.features.transactions.data.repository.TransactionRepository
import dagger.Provides
import javax.inject.Inject


class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke (id: Long): Result<Unit> =
        repository.deleteTransaction(id)
}
