package com.aetherized.smartfinance.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
abstract class BaseDaoTest {
    protected lateinit var database: SmartFinanceDatabase
    protected val dispatcher = StandardTestDispatcher()
    protected val testScope = TestScope(dispatcher)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SmartFinanceDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    fun runBlockingTest(block: suspend TestScope.() -> Unit) = testScope.runTest { block() }
}
