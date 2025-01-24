package com.aetherized.smartfinance.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aetherized.smartfinance.core.database.entity.FinancialGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialGoalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGoal(goal: FinancialGoalEntity): Long

    @Update
    suspend fun updateGoal(goal: FinancialGoalEntity)

    @Query("DELETE FROM financial_goals WHERE id = :id")
    suspend fun deleteGoalById(id: Long)

    @Query("SELECT * FROM financial_goals")
    fun getAllActiveGoals(): Flow<List<FinancialGoalEntity>>
}

