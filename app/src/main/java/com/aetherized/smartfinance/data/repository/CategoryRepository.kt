package com.aetherized.smartfinance.data.repository

import com.aetherized.smartfinance.data.local.dao.CategoryDao
import com.aetherized.smartfinance.data.local.entity.CategoryEntity
import com.aetherized.smartfinance.data.local.relationship.CategoryWithTransactions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {
    fun getAllCategories(limit:Int = 50, offset: Int = 0): Flow<List<CategoryEntity>> =
        categoryDao.getAllCategories(limit, offset)

    fun getCategoryWithTransactions(categoryId: Long): Flow<CategoryWithTransactions> =
        categoryDao.getCategoryWithTransactions(categoryId)

    suspend fun addCategory(category: CategoryEntity): Result<Long> {
        return if (category.name.isNotBlank()) {
            val id = categoryDao.insertCategory(category)
            Result.success(id)
        } else {
            Result.failure(IllegalArgumentException("Category name cannot be empty."))
        }
    }
}
