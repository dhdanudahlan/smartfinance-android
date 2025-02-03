package com.aetherized.smartfinance.features.records.data.di

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.features.categories.domain.repository.CategoryRepository
import com.aetherized.smartfinance.features.categories.data.repository.CategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CategoryDataModule {
    @Provides
    fun provideCategoryRepository(
        categoryDao: CategoryDao
    ): CategoryRepository = CategoryRepositoryImpl(categoryDao)
}
