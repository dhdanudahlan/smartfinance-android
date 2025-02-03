package com.aetherized.smartfinance.features.categories.domain.usecase

import com.aetherized.smartfinance.features.categories.domain.repository.CategoryRepository
import javax.inject.Inject


class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke (id: Long): Result<Unit> =
        repository.deleteCategory(id)
}
