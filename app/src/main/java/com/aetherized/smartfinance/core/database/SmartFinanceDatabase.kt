package com.aetherized.smartfinance.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.core.database.entity.CategoryEntity

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SmartFinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}
