package com.aetherized.smartfinance.features.finance.domain.usecase

import com.aetherized.smartfinance.features.finance.domain.model.TransactionDetails
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
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
    ): Flow<TransactionDetails> {
        return repository.getTransactionById(id).flatMapLatest { transaction ->
            repository.getCategoryById(transaction.categoryId).map { category ->
                TransactionDetails(transaction, category)
            }
        }
    }
}
