package com.aetherized.smartfinance.features.categories.data.repository

import com.aetherized.smartfinance.features.categories.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(limit:Int, offset: Int): Flow<List<Category>>

    suspend fun getCategoryById(id: Long): Result<Category?>

    suspend fun addCategory(category: Category): Result<Long>

    suspend fun updateCategory(category: Category): Result<Unit>

    suspend fun deleteCategory(id: Long): Result<Unit>

}
