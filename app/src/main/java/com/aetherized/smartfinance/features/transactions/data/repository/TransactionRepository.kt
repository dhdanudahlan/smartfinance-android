package com.aetherized.smartfinance.features.transactions.data.repository

import kotlinx.coroutines.flow.Flow
import com.aetherized.smartfinance.features.transactions.domain.model.Transaction

interface TransactionRepository {
    fun getAllTransactions(limit: Int = 50, offset: Int = 0): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Long): Result<Transaction?>

    suspend fun addTransaction(transaction: Transaction): Result<Long>

    suspend fun updateTransaction(transaction: Transaction): Result<Unit>

    suspend fun deleteTransaction(id: Long): Result<Unit>

    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>

}
