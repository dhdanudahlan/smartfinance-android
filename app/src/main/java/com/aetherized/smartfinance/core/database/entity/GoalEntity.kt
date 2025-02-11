package com.aetherized.smartfinance.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "financial_goals"
)
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    @ColumnInfo(name = "target_amount")
    val targetAmount: Int,
    @ColumnInfo(name = "current_amount")
    val currentAmount: Int = 0,
    val deadline: Long,
    @ColumnInfo(name = "is_achieved")
    val isAchieved: Boolean = false
)
