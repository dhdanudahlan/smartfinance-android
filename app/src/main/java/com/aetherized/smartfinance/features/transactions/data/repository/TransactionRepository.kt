package com.aetherized.smartfinance.features.transactions.data.repository

import com.aetherized.smartfinance.features.transactions.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(limit: Int, offset: Int): Flow<List<Transaction>>

    suspend fun getTransactionById(id: Long): Result<Transaction?>

    suspend fun addTransaction(transaction: Transaction): Result<Long>

    suspend fun updateTransaction(transaction: Transaction): Result<Unit>

    suspend fun deleteTransaction(id: Long): Result<Unit>

    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>
}
