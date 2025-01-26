package com.aetherized.smartfinance.features.categories.domain.usecase

import com.aetherized.smartfinance.features.categories.data.repository.CategoryRepository
import com.aetherized.smartfinance.features.categories.domain.model.Category
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke (limit: Int = 50, offset: Int = 0): Flow<List<Category>> =
        repository.getAllCategories(limit, offset)
}
