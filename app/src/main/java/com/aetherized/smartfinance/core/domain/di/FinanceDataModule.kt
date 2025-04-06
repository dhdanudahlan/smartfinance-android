package com.aetherized.smartfinance.core.domain.di

import com.aetherized.smartfinance.core.data.repository.FinanceRepository
import com.aetherized.smartfinance.core.data.repository.FinanceRepositoryImpl
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.domain.usecase.DeleteCategoryUseCase
import com.aetherized.smartfinance.core.domain.usecase.DeleteTransactionUseCase
import com.aetherized.smartfinance.core.domain.usecase.GetCategoriesUseCase
import com.aetherized.smartfinance.core.domain.usecase.GetMonthlyTransactionsUseCase
import com.aetherized.smartfinance.core.domain.usecase.GetTransactionsUseCase
import com.aetherized.smartfinance.core.domain.usecase.UpsertCategoryUseCase
import com.aetherized.smartfinance.core.domain.usecase.UpsertTransactionUseCase
import com.aetherized.smartfinance.core.remote.RemoteDataSource
import com.aetherized.smartfinance.core.remote.RemoteDataSourceImpl
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
    fun provideGetMonthlyTransactionsUseCase(repository: FinanceRepository): GetMonthlyTransactionsUseCase =
        GetMonthlyTransactionsUseCase(repository)

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
