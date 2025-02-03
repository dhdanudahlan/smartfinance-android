package com.aetherized.smartfinance.core.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.utils.SyncStatus
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // To test LiveData/Flow synchronously

    private lateinit var database: SmartFinanceDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            androidx.test.core.app.ApplicationProvider.getApplicationContext(),
            SmartFinanceDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        categoryDao = database.categoryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveCategory() = runBlocking {
        // Create a test category entity.
        val testCategory = CategoryEntity(
            name = "Groceries",
            type = CategoryType.EXPENSE,
            color = "#FF0000",
            icon = "groceries_icon",
            isDeleted = false,
            lastModified = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        )

        // Insert the category.
        val id = categoryDao.upsertCategory(testCategory)
        assert(id > 0)

        // Retrieve the category.
        val retrievedCategory = categoryDao.getCategoryById(id)
        assertNotNull(retrievedCategory)
        assertEquals("Groceries", retrievedCategory?.name)
    }

    @Test
    fun softDeleteCategoryTest() = runBlocking {
        // Insert a category.
        val testCategory = CategoryEntity(
            name = "Utilities",
            type = CategoryType.EXPENSE,
            color = "#00FF00",
            icon = "utilities_icon",
            isDeleted = false,
            lastModified = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        )
        val id = categoryDao.upsertCategory(testCategory)

        // Soft delete the category.
        categoryDao.softDeleteCategoryById(id, System.currentTimeMillis(), SyncStatus.PENDING.name)

        // Verify that the category is not returned in active queries.
        val activeCategories = categoryDao.getAllActiveCategories(50, 0).first()
        val exists = activeCategories.any { it.id == id }
        Assert.assertFalse("Category should be marked as deleted", exists)
    }
}
