package com.aetherized.smartfinance.features.transactions.domain.usecase

import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.features.transactions.data.repository.TransactionRepositoryImpl
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
    suspend operator fun invoke(transaction: TransactionEntity): Result<Long> {
        return transactionRepositoryImpl.addTransaction(transaction)
    }
}
