package com.aetherized.smartfinance.features.goals.data.repository

import com.aetherized.smartfinance.core.database.dao.GoalDao
import com.aetherized.smartfinance.core.database.entity.FinancialEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepository @Inject constructor(private val goalDao: GoalDao) {
    fun getAllGoals(): Flow<List<FinancialEntity>> =
        goalDao.getAllActiveGoals()

    suspend fun addGoal(goal: FinancialEntity): Result<Long> {
        return if (goal.name.isNotBlank()) {
            val id = goalDao.insertGoal(goal)
            Result.success(id)
        } else {
            Result.failure(IllegalArgumentException("Financial Goal name cannot be empty."))
        }
    }

    suspend fun updateGoal(goal: FinancialEntity): Result<Unit> {
        return try {
            goalDao.updateGoal(goal)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteGoal(id: Long): Result<Unit> {
        return try {
            goalDao.deleteGoalById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
