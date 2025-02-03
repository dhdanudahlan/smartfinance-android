package com.aetherized.smartfinance.features.categories.domain.usecase

import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.categories.domain.repository.CategoryRepository
import javax.inject.Inject


class UpsertCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke (category: Category): Result<Long> =
        repository.upsert(category)
}
