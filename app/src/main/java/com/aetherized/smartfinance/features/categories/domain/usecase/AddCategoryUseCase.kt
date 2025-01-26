package com.aetherized.smartfinance.features.categories.domain.usecase

import com.aetherized.smartfinance.features.categories.data.repository.CategoryRepository
import com.aetherized.smartfinance.features.categories.domain.model.Category
import dagger.Provides
import javax.inject.Inject


class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke (category: Category): Result<Long> =
        repository.addCategory(category)
}
