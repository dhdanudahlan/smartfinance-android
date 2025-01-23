package com.aetherized.smartfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aetherized.smartfinance.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    suspend fun getTransactionsWithinDateRange(startDate: String, endDate: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE user_id = :userId")
    fun getTransactionsByUser(userId: Int): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE category_id = :categoryId")
    suspend fun getTransactionsByCategory(categoryId: Int): Flow<List<TransactionEntity>>

    @Query("DELETE FROM transactions WHERE date < :thresholdDate")
    suspend fun deleteOutdatedTransactions(thresholdDate: String): Int

    @Query("SELECT SUM(amount) FROM transactions WHERE category_id = :categoryId")
    suspend fun getTotalAmountByCategory(categoryId: Int): Double

    @Query("SELECT COUNT(*) FROM transactions")
    suspend fun getTransactionsCount(): Int
}