package com.aetherized.smartfinance.features.records.domain.usecase

import com.aetherized.smartfinance.features.records.domain.model.Transaction
import com.aetherized.smartfinance.features.records.domain.repository.RecordsRepository
import javax.inject.Inject

class UpsertTransactionUseCase @Inject constructor(
    private val recordsRepositoryImpl: RecordsRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return recordsRepositoryImpl.saveTransaction(transaction)
    }
}
