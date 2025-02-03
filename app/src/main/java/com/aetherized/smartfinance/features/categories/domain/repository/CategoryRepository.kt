package com.aetherized.smartfinance.features.categories.domain.repository

import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    // === CATEGORY OPERATIONS ===

    fun getActiveCategories(limit:Int = 50, offset: Int = 50): Flow<List<Category>>

    fun getCategoriesByType(type: CategoryType, limit:Int = 50, offset: Int = 50): Flow<List<Category>>

    suspend fun saveCategory(category: Category): Result<Long>

    suspend fun deleteCategory(id: Long): Result<Unit>

    suspend fun upsert(category: Category): Result<Long>

    suspend fun deleteCategory(id: Long): Result<Unit>

}
