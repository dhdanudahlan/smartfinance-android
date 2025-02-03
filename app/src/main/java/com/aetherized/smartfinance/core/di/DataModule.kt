package com.aetherized.smartfinance.core.di

import com.aetherized.smartfinance.core.datastore.UserDataRepository
import com.aetherized.smartfinance.core.datastore.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindUserDataRepository(
        userDataRepository: UserDataRepositoryImpl
    ): UserDataRepository
}
