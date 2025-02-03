package com.aetherized.smartfinance.features.finance.data.di

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.remote.RemoteDataSource
import com.aetherized.smartfinance.core.remote.RemoteDataSourceImpl
import com.aetherized.smartfinance.features.finance.data.repository.FinanceRepositoryImpl
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteCategoryUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.DeleteTransactionUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.GetTransactionsUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertCategoryUseCase
import com.aetherized.smartfinance.features.finance.domain.usecase.UpsertTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FinanceDataModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource = RemoteDataSourceImpl()

    @Provides
    @Singleton
    fun provideFinanceRepository(
        categoryDao: CategoryDao,
        transactionDao: TransactionDao,
        remoteDataSource: RemoteDataSource
    ): FinanceRepository =
        FinanceRepositoryImpl(categoryDao, transactionDao, remoteDataSource)


    @Provides
    fun provideGetCategoriesUseCase(repository: FinanceRepository): GetCategoriesUseCase =
        GetCategoriesUseCase(repository)

    @Provides
    fun provideGetTransactionsUseCase(repository: FinanceRepository): GetTransactionsUseCase =
        GetTransactionsUseCase(repository)

    @Provides
    fun provideDeleteCategoryUseCase(repository: FinanceRepository): DeleteCategoryUseCase =
        DeleteCategoryUseCase(repository)

    @Provides
    fun provideDeleteTransactionsUseCase(repository: FinanceRepository): DeleteTransactionUseCase =
        DeleteTransactionUseCase(repository)

    @Provides
    fun provideUpsertCategoryUseCase(repository: FinanceRepository): UpsertCategoryUseCase =
        UpsertCategoryUseCase(repository)

    @Provides
    fun provideUpsertTransactionsUseCase(repository: FinanceRepository): UpsertTransactionUseCase =
        UpsertTransactionUseCase(repository)


    // TODO: Add similar providers for save/delete use cases as needed.
}
