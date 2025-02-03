package com.aetherized.smartfinance.features.records.domain.usecase

import com.aetherized.smartfinance.features.records.domain.repository.RecordsRepository
import javax.inject.Inject


class DeleteTransactionUseCase @Inject constructor(
    private val repository: RecordsRepository
) {
    suspend operator fun invoke (id: Long): Result<Unit> =
        repository.deleteTransaction(id)
}
