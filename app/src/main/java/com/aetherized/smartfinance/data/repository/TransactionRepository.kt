package com.aetherized.smartfinance.data.repository

import com.aetherized.smartfinance.data.local.dao.TransactionDao
import com.aetherized.smartfinance.data.local.entity.CategoryEntity
import com.aetherized.smartfinance.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {
    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByCategory(categoryId)

    suspend fun addTransaction(transaction: TransactionEntity): Result<Long> {
        return if (transaction.amount > 0) {
            val id = transactionDao.insertTransaction(transaction)
            Result.success(id)
        } else {
            Result.failure(IllegalArgumentException("Transaction amount must be greater than zero."))
        }
    }
}
