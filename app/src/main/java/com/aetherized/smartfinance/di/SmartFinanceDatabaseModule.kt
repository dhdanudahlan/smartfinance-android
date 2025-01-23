package com.aetherized.smartfinance.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aetherized.smartfinance.data.local.dao.CategoryDao
import com.aetherized.smartfinance.data.local.dao.TransactionDao
import com.aetherized.smartfinance.data.local.dao.UserDao
import com.aetherized.smartfinance.data.local.database.SmartFinanceDatabase
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
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Provides
    fun provideUserDao(database: SmartFinanceDatabase): UserDao {
        return database.userDao()
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
val MIGRATION_1_2 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }
}