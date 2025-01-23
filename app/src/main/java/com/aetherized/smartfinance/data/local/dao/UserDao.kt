package com.aetherized.smartfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aetherized.smartfinance.data.local.entity.UserEntity
import com.aetherized.smartfinance.data.local.relationship.UserWithTransactions
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int): Int

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserWithTransactions(userId: Int): Flow<UserWithTransactions>
}