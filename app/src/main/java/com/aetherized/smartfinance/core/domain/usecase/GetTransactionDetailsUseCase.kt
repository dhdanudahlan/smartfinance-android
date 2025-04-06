package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.model.data.TransactionForm
import com.aetherized.smartfinance.core.data.repository.FinanceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetTransactionDetailsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke (
        id: Long
    ): Flow<TransactionForm> {
        return repository.getTransactionById(id).flatMapLatest { transaction ->
            repository.getCategoryById(transaction.categoryId).map { category ->
                TransactionForm(transaction, category)
            }
        }
    }
}
