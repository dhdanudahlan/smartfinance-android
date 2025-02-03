package com.aetherized.smartfinance.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoInstrumentedTest {

    private lateinit var db: SmartFinanceDatabase
    private lateinit var dao: CategoryDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SmartFinanceDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.categoryDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetCategory() = runBlocking {
        // Given
        val category = CategoryEntity(
            name = "Groceries",
            type = CategoryType.EXPENSE,
            color = "#FF0000"
        )

        // When
        val insertedId = dao.upsertCategory(category)

        // Then
        val loadedCategory = dao.getCategoryById(insertedId)
        assertNotNull(loadedCategory)
        assertEquals("Groceries", loadedCategory?.name)
        assertEquals(CategoryType.EXPENSE, loadedCategory?.type)
    }

    @Test
    fun updateCategory() = runBlocking {
        // Insert
        val category = CategoryEntity(
            name = "Shopping",
            type = CategoryType.EXPENSE
        )
        val insertedId = dao.upsertCategory(category)

        // Update
        val updatedCategory = CategoryEntity(
            id = insertedId,
            name = "New Shopping",
            type = CategoryType.EXPENSE,
            lastModified = System.currentTimeMillis()
        )
        dao.upsertCategory(updatedCategory)

        // Verify
        val loadedCategory = dao.getCategoryById(insertedId)
        assertEquals("New Shopping", loadedCategory?.name)
    }

    @Test
    fun deleteCategory() = runBlocking {
        // Insert
        val category = CategoryEntity(
            name = "Bills",
            type = CategoryType.EXPENSE
        )
        val insertedId = dao.upsertCategory(category)

        // Delete
        dao.deleteCategoryById(insertedId)

        // Verify
        val loadedCategory = dao.getCategoryById(insertedId)
        assertNull(loadedCategory)
    }

    @Test
    fun getAllActiveCategories() = runBlocking {
        // Insert a couple of active categories
        dao.upsertCategory(CategoryEntity(name = "Category A", type = CategoryType.EXPENSE))
        dao.upsertCategory(CategoryEntity(name = "Category B", type = CategoryType.EXPENSE))
        // Insert a deleted category
        dao.upsertCategory(CategoryEntity(name = "Category Deleted", type = CategoryType.EXPENSE, isDeleted = true))

        // Flow usage: collect the flow and test the emitted values
        val categoriesFlow = dao.getAllActiveCategories()
        val job = launch {
            categoriesFlow.collect { categories ->
                // We expect 2 active categories
                assertEquals(2, categories.size)
                // Optional: cancel collection after verification
                cancel()
            }
        }
        job.join()
    }
}
