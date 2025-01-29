package com.aetherized.smartfinance.core.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.utils.CategoryType
import com.aetherized.smartfinance.core.utils.TransactionValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SmartFinanceDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartFinanceDatabase {
        return Room.databaseBuilder(
            context,
            SmartFinanceDatabase::class.java,
            "smartfinance_database"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val predefinedCategories = listOf(
                    CategoryEntity(name = "Other Expenses", type = CategoryType.EXPENSE, color = "#FF0000"),
                    CategoryEntity(name = "Other Incomes", type = CategoryType.INCOME, color = "#00FF00")
                )
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val database = provideDatabase(context)
                        predefinedCategories.forEach { database.categoryDao().insertCategory(it) }
                    } catch (e: Exception) {
                        Log.e("DatabaseCallback", "Error seeding database: ", e)
                    }
                }
            }
        }).fallbackToDestructiveMigration().build()
    }
}
