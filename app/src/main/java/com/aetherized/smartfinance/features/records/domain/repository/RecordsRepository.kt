package com.aetherized.smartfinance.features.records.domain.repository

import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import com.aetherized.smartfinance.features.records.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface RecordsRepository {

    // === CATEGORY OPERATIONS ===

    fun getActiveCategories(limit:Int = 50, offset: Int = 50): Flow<List<Category>>

    fun getCategoriesByType(type: CategoryType, limit:Int = 50, offset: Int = 50): Flow<List<Category>>

    suspend fun saveCategory(category: Category): Result<Long>

    suspend fun deleteCategory(id: Long): Result<Unit>


    // === TRANSACTION OPERATIONS ===

    fun getTransactions(limit: Int, offset: Int): Flow<List<Transaction>>

    fun getTransactionsByCategory(categoryId: Long, limit: Int, offset: Int): Flow<List<Transaction>>

    fun getTransactionsByType(categoryType: CategoryType, limit: Int, offset: Int): Flow<List<Transaction>>

    suspend fun saveTransaction(transaction: Transaction): Result<Long>

    suspend fun deleteTransaction(id: Long): Result<Unit>


    // === CLOUD SYNC ===

    suspend fun syncData(): Result<Unit>

}
