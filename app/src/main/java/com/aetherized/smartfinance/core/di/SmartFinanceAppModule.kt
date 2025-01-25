package com.aetherized.smartfinance.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.utils.CategoryType
import com.aetherized.smartfinance.core.utils.TransactionValidator
import com.aetherized.smartfinance.features.transactions.data.repository.TransactionRepository
import com.aetherized.smartfinance.features.transactions.data.repository.TransactionRepositoryImpl
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
object SmartFinanceAppModule {


    @Provides
    fun provideCategoryDao(database: SmartFinanceDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideTransactionDao(database: SmartFinanceDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao)
    }
}
