package com.aetherized.smartfinance.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.remote.RemoteDataSource
import com.aetherized.smartfinance.core.remote.RemoteDataSourceImpl
import com.aetherized.smartfinance.features.finance.data.repository.FinanceRepositoryImpl
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FinanceRepositoryTest {

    private lateinit var database: SmartFinanceDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var repository: FinanceRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SmartFinanceDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()
        transactionDao = database.transactionDao()
        remoteDataSource = RemoteDataSourceImpl()
        repository = FinanceRepositoryImpl(categoryDao, transactionDao, remoteDataSource)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSaveAndRetrieveCategory() = runBlocking {
        // Assume you have a helper to create a domain Category with a corresponding toEntity() method.
        val testCategory = Category(
            id = 0L,
            name = "Test Category",
            type = CategoryType.EXPENSE,
            color = "#ABCDEF",
            icon = "test_icon",
            isDeleted = false,
        )

        val result = repository.saveCategory(testCategory)
        assertTrue(result.isSuccess)
        val savedId = result.getOrNull()!!

        // Retrieve using repository.
        val categories = repository.getActiveCategories(50, 0)
            .first()  // Use kotlinx.coroutines.flow.first() to get the first emission.
        val exists = categories.any { it.id == savedId }
        assertTrue("Saved category should be in active list", exists)
    }

    // Additional tests for deletion, transaction operations, etc.
}
