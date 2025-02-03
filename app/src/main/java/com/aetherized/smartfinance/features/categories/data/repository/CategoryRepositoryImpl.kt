package com.aetherized.smartfinance.features.categories.data.repository

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.utils.CategoryValidator
import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import com.aetherized.smartfinance.features.categories.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {
    override fun getActiveCategories(limit:Int, offset: Int): Flow<List<Category>> =
        categoryDao.getAllActiveCategories(limit, offset)
            .map { entities -> entities.map { it.toDomainModel() } }

    override fun getCategoriesByType(type: CategoryType, limit:Int, offset: Int): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type.name, limit, offset)
            .map { entities -> entities.map { it.toDomainModel() } }


    override suspend fun upsert(category: Category): Result<Long> {
        return try {
            CategoryValidator.validateName(category.toEntity())
            val id = categoryDao.upsertCategory(category.toEntity())
            Result.success(id)
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
