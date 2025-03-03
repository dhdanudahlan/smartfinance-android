package com.aetherized.smartfinance.features.finance.domain.repository

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {

    // === CATEGORY OPERATIONS ===

    fun getActiveCategories(limit:Int = 50, offset: Int = 0): Flow<List<Category>>

    fun getCategoriesByType(type: CategoryType, limit:Int = 50, offset: Int = 0): Flow<List<Category>>

    fun getCategoryById(id: Long):Flow<Category>

    suspend fun saveCategory(category: Category): Result<Long>

    suspend fun deleteCategory(id: Long): Result<Unit>


    // === TRANSACTION OPERATIONS ===

    fun getActiveTransactions(limit: Int, offset: Int): Flow<List<Transaction>>

    fun getTransactionsByCategory(categoryId: Long, limit: Int, offset: Int): Flow<List<Transaction>>

    fun getTransactionsByType(categoryType: CategoryType, limit: Int, offset: Int): Flow<List<Transaction>>

    fun getTransactionById(id: Long):Flow<Transaction>

    suspend fun saveTransaction(transaction: Transaction): Result<Long>

    suspend fun deleteTransaction(id: Long): Result<Unit>


    // === CLOUD SYNC ===

    suspend fun syncData(): Result<Unit>

}
