package com.aetherized.smartfinance.features.transactions.domain.usecase

import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.features.transactions.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    /**
     * Tests should cover the following edge cases:
     * - Duplicate transactions with the same data.
     * - Transactions with invalid or missing fields.
     * - Valid transactions to ensure proper insertion.
     */
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: TransactionEntity): Result<Long> {
        return transactionRepository.addTransaction(transaction)
    }
}
