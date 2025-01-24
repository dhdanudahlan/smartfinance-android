package com.aetherized.smartfinance.core.di

import android.content.Context
import androidx.room.Room
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        ).build()
    }

    @Provides
    fun provideCategoryDao(database: SmartFinanceDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideTransactionDao(database: SmartFinanceDatabase): TransactionDao {
        return database.transactionDao()
    }
}
