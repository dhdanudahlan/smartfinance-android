package com.aetherized.smartfinance.core.network.di


import com.aetherized.smartfinance.core.network.di.SmartFinanceDispatchers
import com.aetherized.smartfinance.core.network.di.SmartFinanceDispatchers.Default
import com.aetherized.smartfinance.core.network.di.SmartFinanceDispatchers.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
