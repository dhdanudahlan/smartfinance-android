package com.aetherized.smartfinance.features.finance.domain.usecase

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import javax.inject.Inject


class UpsertCategoryUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke (category: Category): Result<Long> =
        repository.saveCategory(category)
}
