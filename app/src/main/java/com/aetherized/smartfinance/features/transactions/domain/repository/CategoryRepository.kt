package com.aetherized.smartfinance.features.transactions.domain.repository

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.database.relationship.CategoryWithTransactions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao) {
    fun getAllCategories(limit:Int = 50, offset: Int = 0): Flow<List<CategoryEntity>> =
        categoryDao.getAllActiveCategories(limit, offset)

    suspend fun getCategoryById(id: Long): Result<CategoryEntity?> {
        return try {
            Result.success(categoryDao.getCategoryById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCategory(category: CategoryEntity): Result<Long> {
        return if (category.name.isNotBlank()) {
            val id = categoryDao.insertCategory(category)
            Result.success(id)
        } else {
            Result.failure(IllegalArgumentException("Category name cannot be empty."))
        }
    }

    suspend fun updateCategory(category: CategoryEntity): Result<Unit> {
        return try {
            categoryDao.updateCategory(category)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCategory(id: Long): Result<Unit> {
        return try {
            categoryDao.deleteCategoryById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
