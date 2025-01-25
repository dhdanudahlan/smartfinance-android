package com.aetherized.smartfinance.features.transactions.data.repository

import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.utils.TransactionValidator
import com.aetherized.smartfinance.features.transactions.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
) : TransactionRepository {
    override fun getAllTransactions(limit: Int, offset: Int): Flow<List<Transaction>> =
        transactionDao.getAllActiveTransactions(limit, offset)
            .map { entities -> entities.map { it.toDomainModel() } }


    override suspend fun getTransactionById(id: Long): Result<Transaction?> {
        return try {
            Result.success(
                transactionDao.getTransactionById(id)?.toDomainModel()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTransaction(transaction: Transaction): Result<Long> {
        return try {
            TransactionValidator.validateAmount(transaction.toEntity())
            val id = transactionDao.insertTransaction(transaction.toEntity())
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        return try {
            transactionDao.updateTransaction(transaction.toEntity())
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

    override fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategory(categoryId)
            .map { entities -> entities.map { it.toDomainModel() } }

}
