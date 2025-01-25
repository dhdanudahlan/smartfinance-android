package com.aetherized.smartfinance.features.transactions.data.repository

import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.core.utils.TransactionValidator
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    suspend fun getTransactionById(id: Long): Result<TransactionEntity?>

    suspend fun addTransaction(transaction: TransactionEntity): Result<Long>

    suspend fun updateTransaction(transaction: TransactionEntity): Result<Unit>

    suspend fun deleteTransaction(id: Long): Result<Unit>

    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>>

}
