package com.aetherized.smartfinance.features.transactions.domain.repository

import com.aetherized.smartfinance.core.database.dao.FinancialGoalDao
import com.aetherized.smartfinance.core.database.entity.FinancialGoalEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinancialGoalRepository @Inject constructor(private val financialGoalDao: FinancialGoalDao) {
    fun getAllFinancialGoals(): Flow<List<FinancialGoalEntity>> =
        financialGoalDao.getAllActiveGoals()

    suspend fun addFinancialGoal(goal: FinancialGoalEntity): Result<Long> {
        return if (goal.name.isNotBlank()) {
            val id = financialGoalDao.insertGoal(goal)
            Result.success(id)
        } else {
            Result.failure(IllegalArgumentException("Financial Goal name cannot be empty."))
        }
    }

    suspend fun updateFinancialGoal(goal: FinancialGoalEntity): Result<Unit> {
        return try {
            financialGoalDao.updateGoal(goal)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFinancialGoal(id: Long): Result<Unit> {
        return try {
            financialGoalDao.deleteGoalById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
