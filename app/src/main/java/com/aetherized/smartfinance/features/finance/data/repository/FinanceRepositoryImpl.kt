package com.aetherized.smartfinance.features.finance.data.repository

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.remote.RemoteDataSource
import com.aetherized.smartfinance.core.utils.CategoryValidator
import com.aetherized.smartfinance.core.utils.ErrorHandler
import com.aetherized.smartfinance.core.utils.SyncStatus
import com.aetherized.smartfinance.core.utils.TransactionValidator
import com.aetherized.smartfinance.core.utils.toDomainModel
import com.aetherized.smartfinance.core.utils.toEntity
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FinanceRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao,
    private val remoteDataSource: RemoteDataSource
) : FinanceRepository {

    // === CATEGORY OPERATIONS ===

    override fun getActiveCategories(limit: Int, offset: Int): Flow<List<Category>> =
        categoryDao.getAllActiveCategories(limit, offset)
            .map { list -> list.map { it.toDomainModel() } }

    override fun getCategoriesByType(type: CategoryType, limit: Int, offset: Int): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type.name, limit, offset)
            .map { list -> list.map { it.toDomainModel() } }


    override fun getCategoryById(id: Long): Flow<Category> =
        categoryDao.getCategoryById(id)
            .map { it.toDomainModel() }

    override suspend fun saveCategory(category: Category): Result<Long> {
        return try {
            // Convert to entity, update metadata, and validate
            val entity = category.toEntity().copy(
                lastModified = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING
            )
            CategoryValidator.validateName(entity)
            val id = categoryDao.upsertCategory(entity)
            Result.success(id)
        } catch (e: Exception) {
            ErrorHandler.logError(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(id: Long): Result<Unit> {
        return try {
            // Soft delete: update the transaction is better than removing it?
            categoryDao.softDeleteCategoryById(
                id = id,
                timestamp = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING.name
            )
            Result.success(Unit)
        } catch (e: Exception) {
            ErrorHandler.logError(e)
            Result.failure(e)
        }
    }

    // === TRANSACTION OPERATIONS ===

    override fun getTransactions(limit: Int, offset: Int): Flow<List<Transaction>> =
        transactionDao.getAllActiveTransactions(limit, offset)
            .map { list -> list.map { it.toDomainModel() } }

    override fun getTransactionsByCategory(categoryId: Long, limit: Int, offset: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategoryId(categoryId, limit, offset)
            .map { list -> list.map { it.toDomainModel() } }

    override fun getTransactionsByType(categoryType: CategoryType, limit: Int, offset: Int): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategoryType(categoryType.name, limit, offset)
            .map { list -> list.map { it.toDomainModel() } }


    override fun getTransactionById(id: Long): Flow<Transaction> =
        transactionDao.getTransactionById(id)
            .map { it.toDomainModel() }


    override suspend fun saveTransaction(transaction: Transaction): Result<Long> {
        return try {
            val entity = transaction.toEntity().copy(
                lastModified = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING
            )
            TransactionValidator.validateAmount(entity)
            // Using REPLACE for upsert consistency
            val id = transactionDao.upsertTransaction(entity)
            Result.success(id)
        } catch (e: Exception) {
            ErrorHandler.logError(e)
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(id: Long): Result<Unit> {
        return try {
            transactionDao.softDeleteTransactionById(
                id = id,
                timestamp = System.currentTimeMillis(),
                syncStatus = SyncStatus.PENDING.name
            )
            Result.success(Unit)
        } catch (e: Exception) {
            ErrorHandler.logError(e)
            Result.failure(e)
        }
    }

    // === CLOUD SYNC ===
    // TODO: Add cloud-sync feature
    override suspend fun syncData(): Result<Unit> {
        return try {
            // 1. Query for local pending changes (both categories and transactions)
            // 2. Upload them to your remote data source
            // 3. Download remote changes
            // 4. Merge remote changes with local data (handle conflicts based on lastModified)
//            remoteDataSource.syncData()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
