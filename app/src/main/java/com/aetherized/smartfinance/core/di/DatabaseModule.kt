package com.aetherized.smartfinance.core.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
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
internal object DatabaseModule {
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
                    CategoryEntity(name = "Others", type = CategoryType.EXPENSE, color = "#FF0000"),
                    CategoryEntity(name = "Others", type = CategoryType.INCOME, color = "#00FF00")
                )
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val database = provideDatabase(context)
                        predefinedCategories.forEach { database.categoryDao().upsertCategory(it) }
                    } catch (e: Exception) {
                        Log.e("DatabaseCallback", "Error seeding database: ", e)
                    }
                }
            }
        }).fallbackToDestructiveMigration().build()
    }
}
