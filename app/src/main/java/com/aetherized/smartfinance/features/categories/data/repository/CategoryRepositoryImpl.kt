package com.aetherized.smartfinance.features.categories.data.repository

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.utils.CategoryValidator
import com.aetherized.smartfinance.features.categories.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {
    override fun getAllCategories(limit:Int, offset: Int): Flow<List<Category>> =
        categoryDao.getAllActiveCategories(limit, offset)
            .map { entities -> entities.map { it.toDomainModel() } }

    override suspend fun getCategoryById(id: Long): Result<Category?> {
        return try {
            Result.success(categoryDao.getCategoryById(id)?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addCategory(category: Category): Result<Long> {
        return try {
            CategoryValidator.validateName(category.toEntity())
            val id = categoryDao.insertCategory(category.toEntity())
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCategory(category: Category): Result<Unit> {
        return try {
            categoryDao.updateCategory(category.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(id: Long): Result<Unit> {
        return try {
            categoryDao.deleteCategoryById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
