package com.aetherized.smartfinance.core.network.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val smartFinanceDispatcher: SmartFinanceDispatchers)

enum class SmartFinanceDispatchers {
    Default,
    IO
}
