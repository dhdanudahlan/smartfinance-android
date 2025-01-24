package com.aetherized.smartfinance.features.transactions.domain.repository

import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {
    fun getAllTransactions(): Flow<List<TransactionEntity>> =
        transactionDao.getAllActiveTransactions()

    suspend fun getTransactionById(id: Long): Result<TransactionEntity?> {
        return try {
            Result.success(transactionDao.getTransactionById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTransaction(transaction: TransactionEntity): Result<Long> {
        return if (transaction.amount > 0) {
            try {
                val id = transactionDao.insertTransaction(transaction)
                Result.success(id)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(IllegalArgumentException("Transaction amount must be greater than zero."))
        }
    }

    suspend fun updateTransaction(transaction: TransactionEntity): Result<Unit> {
        return try {
            transactionDao.updateTransaction(transaction)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTransaction(id: Long): Result<Unit> {
        return try {
            transactionDao.deleteTransactionById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByCategory(categoryId)

}
