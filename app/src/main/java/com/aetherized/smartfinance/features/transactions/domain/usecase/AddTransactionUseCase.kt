package com.aetherized.smartfinance.features.transactions.domain.usecase

import com.aetherized.smartfinance.features.transactions.data.repository.TransactionRepositoryImpl
import com.aetherized.smartfinance.features.transactions.domain.model.Transaction
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    /**
     * Tests should cover the following edge cases:
     * - Duplicate transactions with the same data.
     * - Transactions with invalid or missing fields.
     * - Valid transactions to ensure proper insertion.
     */
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return transactionRepositoryImpl.addTransaction(transaction)
    }
}
