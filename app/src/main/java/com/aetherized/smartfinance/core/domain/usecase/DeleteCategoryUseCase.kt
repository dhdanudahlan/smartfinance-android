package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.data.repository.FinanceRepository
import javax.inject.Inject


class DeleteCategoryUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke (id: Long): Result<Unit> =
        repository.deleteCategory(id)
}
