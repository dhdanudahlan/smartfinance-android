package com.aetherized.smartfinance.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var database: SmartFinanceDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SmartFinanceDatabase::class.java
        ).allowMainThreadQueries().build()

        categoryDao = database.categoryDao()
        transactionDao = database.transactionDao()

        val predefinedCategories = listOf(
            CategoryEntity(name = "Other Expenses", type = CategoryType.EXPENSE, color = "#FF0000"),
            CategoryEntity(name = "Other Incomes", type = CategoryType.INCOME, color = "#00FF00")
        )

        CoroutineScope(Dispatchers.IO).launch {
            predefinedCategories.forEach { categoryDao.upsertCategory(it) }
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun predefinedCategoriesInserted_correctlyPopulated() = runBlocking {
        val categories = database.categoryDao().getAllActiveCategories().first()
        assertEquals(2, categories.size)
        assertTrue(categories.any { it.name == "Other Expenses" })
        assertTrue(categories.any { it.name == "Other Incomes" })
    }
}

