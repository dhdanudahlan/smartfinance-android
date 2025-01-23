package com.aetherized.smartfinance.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aetherized.smartfinance.data.local.dao.CategoryDao
import com.aetherized.smartfinance.data.local.dao.TransactionDao
import com.aetherized.smartfinance.data.local.dao.UserDao
import com.aetherized.smartfinance.data.local.entity.TransactionEntity
import com.aetherized.smartfinance.data.local.entity.UserEntity
import com.aetherized.smartfinance.data.local.entity.CategoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class, UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SmartFinanceDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao

    companion object {

    }
}