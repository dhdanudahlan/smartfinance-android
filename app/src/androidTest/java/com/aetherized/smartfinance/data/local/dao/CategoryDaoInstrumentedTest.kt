package com.aetherized.smartfinance.data.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoInstrumentedTest {

//    private lateinit var db: SmartFinanceDatabase
//    private lateinit var dao: CategoryDao
//
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(
//            context,
//            SmartFinanceDatabase::class.java
//        ).allowMainThreadQueries().build()
//        dao = db.categoryDao()
//    }
//
//    @After
//    fun closeDb() {
//        db.close()
//    }
//
//    @Test
//    fun insertAndGetCategory() = runBlocking {
//        // Given
//        val category = CategoryEntity(
//            name = "Groceries",
//            type = CategoryType.EXPENSE,
//            color = "#FF0000"
//        )
//
//        // When
//        val insertedId = dao.upsertCategory(category)
//
//        // Then
//        val loadedCategory = dao.getCategoryById(insertedId)
//        assertNotNull(loadedCategory)
//        assertEquals("Groceries", loadedCategory?.name)
//        assertEquals(CategoryType.EXPENSE, loadedCategory?.type)
//    }
//
//    @Test
//    fun updateCategory() = runBlocking {
//        // Insert
//        val category = CategoryEntity(
//            name = "Shopping",
//            type = CategoryType.EXPENSE
//        )
//        val insertedId = dao.upsertCategory(category)
//
//        // Update
//        val updatedCategory = CategoryEntity(
//            id = insertedId,
//            name = "New Shopping",
//            type = CategoryType.EXPENSE,
//            lastModified = System.currentTimeMillis()
//        )
//        dao.upsertCategory(updatedCategory)
//
//        // Verify
//        val loadedCategory = dao.getCategoryById(insertedId)
//        assertEquals("New Shopping", loadedCategory?.name)
//    }
//
//    @Test
//    fun deleteCategory() = runBlocking {
//        // Insert
//        val category = CategoryEntity(
//            name = "Bills",
//            type = CategoryType.EXPENSE
//        )
//        val insertedId = dao.upsertCategory(category)
//
//        // Delete
//        dao.softDeleteCategoryById(insertedId)
//
//        // Verify
//        val loadedCategory = dao.getCategoryById(insertedId)
//        assertNull(loadedCategory)
//    }
//
//    @Test
//    fun getAllActiveCategories() = runBlocking {
//        // Insert a couple of active categories
//        dao.upsertCategory(CategoryEntity(name = "Category A", type = CategoryType.EXPENSE))
//        dao.upsertCategory(CategoryEntity(name = "Category B", type = CategoryType.EXPENSE))
//        // Insert a deleted category
//        dao.upsertCategory(CategoryEntity(name = "Category Deleted", type = CategoryType.EXPENSE, isDeleted = true))
//
//        // Flow usage: collect the flow and test the emitted values
//        val categoriesFlow = dao.getAllActiveCategories()
//        val job = launch {
//            categoriesFlow.collect { categories ->
//                // We expect 2 active categories
//                assertEquals(2, categories.size)
//                // Optional: cancel collection after verification
//                cancel()
//            }
//        }
//        job.join()
//    }
}
