package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.model.data.Category
import com.aetherized.smartfinance.core.data.repository.FinanceRepository
import javax.inject.Inject


class UpsertCategoryUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke (category: Category): Result<Long> =
        repository.saveCategory(category)
}
