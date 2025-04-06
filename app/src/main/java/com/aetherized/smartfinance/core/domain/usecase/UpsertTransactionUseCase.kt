package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.model.data.Transaction
import com.aetherized.smartfinance.core.data.repository.FinanceRepository
import javax.inject.Inject

class UpsertTransactionUseCase @Inject constructor(
    private val financeRepositoryImpl: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return financeRepositoryImpl.saveTransaction(transaction)
    }
}
