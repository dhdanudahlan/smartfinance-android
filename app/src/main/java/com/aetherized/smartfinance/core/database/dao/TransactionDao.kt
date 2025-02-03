package com.aetherized.smartfinance.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.core.utils.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTransaction(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllTransactions(transactions: List<TransactionEntity>): List<Long>

    @Query("UPDATE transactions SET is_deleted = 1, last_modified = :timestamp, sync_status = :syncStatus WHERE id = :id")
    suspend fun softDeleteTransactionById(id: Long, timestamp: Long = System.currentTimeMillis(), syncStatus: String = SyncStatus.PENDING.name)

    @Query("SELECT * FROM transactions WHERE is_deleted = 0 ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getAllActiveTransactions(limit: Int = 50, offset: Int = 0): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId AND is_deleted = 0 ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getTransactionsByCategoryId(categoryId: Long, limit: Int = 50, offset: Int = 0): Flow<List<TransactionEntity>>

    @Query("""
        SELECT t.* FROM transactions t 
        JOIN categories c ON t.categoryId = c.id 
        WHERE c.type = :categoryType AND c.is_deleted = 0 AND t.is_deleted = 0 
        ORDER BY t.timestamp DESC LIMIT :limit OFFSET :offset
    """)
    fun getTransactionsByCategoryType(categoryType: String, limit: Int = 50, offset: Int = 0): Flow<List<TransactionEntity>>
}
