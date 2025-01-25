package com.aetherized.smartfinance.features.transactions.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.utils.CategoryType
import com.aetherized.smartfinance.features.categories.data.repository.CategoryRepositoryImpl
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryRepositoryImplInstrumentedTest {

    private lateinit var db: SmartFinanceDatabase
    private lateinit var dao: CategoryDao
    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SmartFinanceDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.categoryDao()
        repository = CategoryRepositoryImpl(dao)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun addCategory_successful() = runBlocking {
        // When
        val result = repository.addCategory(CategoryEntity(name = "Travel", type = CategoryType.EXPENSE))

        // Then
        assertTrue(result.isSuccess)
        val insertedId = result.getOrNull()
        assertNotNull(insertedId)

        // Confirm that the DB has the new category
        val dbCategory = dao.getCategoryById(insertedId!!)
        assertEquals("Travel", dbCategory?.name)
    }

    @Test
    fun addCategory_failure_blankName() = runBlocking {
        // When
        val result = repository.addCategory(CategoryEntity(name = "", type = CategoryType.EXPENSE))

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
    }

    @Test
    fun updateCategory() = runBlocking {
        // Insert first
        val insertResult = repository.addCategory(CategoryEntity(name = "Initial", type = CategoryType.EXPENSE))
        val id = insertResult.getOrNull()!!

        // Update
        val updatedCategory = CategoryEntity(
            id = id,
            name = "Updated",
            type = CategoryType.EXPENSE,
            lastModified = System.currentTimeMillis()
        )
        val updateResult = repository.updateCategory(updatedCategory)

        // Verify
        assertTrue(updateResult.isSuccess)
        val dbCategory = dao.getCategoryById(id)
        assertEquals("Updated", dbCategory?.name)
    }

    @Test
    fun deleteCategory() = runBlocking {
        // Insert
        val insertResult = repository.addCategory(CategoryEntity(name = "Removable", type = CategoryType.EXPENSE))
        val id = insertResult.getOrNull()!!

        // Delete
        val deleteResult = repository.deleteCategory(id)
        assertTrue(deleteResult.isSuccess)

        // Verify
        val dbCategory = dao.getCategoryById(id)
        assertNull(dbCategory)
    }

    @Test
    fun getAllCategories() = runBlocking {
        // Insert
        repository.addCategory(CategoryEntity(name = "Cat A", type = CategoryType.EXPENSE))
        repository.addCategory(CategoryEntity(name = "Cat B", type = CategoryType.INCOME))

        // Because it's a Flow, let's collect it once
        val flow = repository.getAllCategories()
        val job = launch {
            flow.collect { categoryList ->
                assertTrue(categoryList.size >= 2)
                // You can assert on names, types, etc.
                cancel() // Stop collecting after verifying once
            }
        }
        job.join()
    }
}
