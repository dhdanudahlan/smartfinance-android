package com.aetherized.smartfinance.features.records.data.di

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.features.records.data.repository.RecordsRepositoryImpl
import com.aetherized.smartfinance.features.records.domain.repository.RecordsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecordsDataModule {
    @Provides
    fun provideTransactionRepository(
        dao: TransactionDao,
        categoryDao: CategoryDao
    ) : RecordsRepository = RecordsRepositoryImpl(dao, categoryDao)
}
