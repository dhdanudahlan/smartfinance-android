package com.aetherized.smartfinance.features.finance.domain.usecase

import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import javax.inject.Inject

class UpsertTransactionUseCase @Inject constructor(
    private val financeRepositoryImpl: FinanceRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return financeRepositoryImpl.saveTransaction(transaction)
    }
}
