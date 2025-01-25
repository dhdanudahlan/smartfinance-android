package com.aetherized.smartfinance.features.transactions.data.repository

import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.core.utils.TransactionValidator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<TransactionEntity>> =
        transactionDao.getAllActiveTransactions()

    override suspend fun getTransactionById(id: Long): Result<TransactionEntity?> {
        return try {
            Result.success(transactionDao.getTransactionById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTransaction(transaction: TransactionEntity): Result<Long> {
        return try {
            TransactionValidator.validateAmount(transaction)
            val id = transactionDao.insertTransaction(transaction)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(transaction: TransactionEntity): Result<Unit> {
        return try {
            transactionDao.updateTransaction(transaction)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(id: Long): Result<Unit> {
        return try {
            transactionDao.deleteTransactionById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByCategory(categoryId)

}
