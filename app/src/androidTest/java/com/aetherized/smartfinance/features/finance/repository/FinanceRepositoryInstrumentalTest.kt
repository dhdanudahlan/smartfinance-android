package com.aetherized.smartfinance.features.finance.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.remote.RemoteDataSourceImpl
import com.aetherized.smartfinance.features.finance.data.repository.FinanceRepositoryImpl
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FinanceRepositoryImplInstrumentedTest {

    private lateinit var db: SmartFinanceDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var repository: FinanceRepositoryImpl
    private lateinit var remoteDataSource: RemoteDataSourceImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SmartFinanceDatabase::class.java
        ).allowMainThreadQueries().build()

        categoryDao = db.categoryDao()
        transactionDao = db.transactionDao()
        repository = FinanceRepositoryImpl(categoryDao, transactionDao, remoteDataSource)
    }

    @After
    fun teardown() {
        db.close()
    }
//
//    @Test
//    fun addCategory_successful() = runBlocking {
//        // When
//        val result = repository.addCategory(CategoryEntity(name = "Travel", type = CategoryType.EXPENSE).toDomainModel())
//
//        // Then
//        assertTrue(result.isSuccess)
//        val insertedId = result.getOrNull()
//        assertNotNull(insertedId)
//
//        // Confirm that the DB has the new category
//        val dbCategory = categoryDao.getCategoryById(insertedId!!)
//        assertEquals("Travel", dbCategory?.name)
//    }
//
//    @Test
//    fun addCategory_failure_blankName() = runBlocking {
//        // When
//        val result = repository.addCategory(CategoryEntity(name = "", type = CategoryType.EXPENSE).toDomainModel())
//
//        // Then
//        assertTrue(result.isFailure)
//        val exception = result.exceptionOrNull()
//        assertTrue(exception is IllegalArgumentException)
//    }
//
//    @Test
//    fun upsert() = runBlocking {
//        // Insert first
//        val insertResult = repository.addCategory(CategoryEntity(name = "Initial", type = CategoryType.EXPENSE).toDomainModel())
//        val id = insertResult.getOrNull()!!
//
//        // Update
//        val updatedCategory = CategoryEntity(
//            id = id,
//            name = "Updated",
//            type = CategoryType.EXPENSE,
//            lastModified = System.currentTimeMillis()
//        ).toDomainModel()
//        val updateResult = repository.upsert(updatedCategory)
//
//        // Verify
//        assertTrue(updateResult.isSuccess)
//        val dbCategory = categoryDao.getCategoryById(id)
//        assertEquals("Updated", dbCategory?.name)
//    }
//
//    @Test
//    fun deleteCategory() = runBlocking {
//        // Insert
//        val insertResult = repository.addCategory(CategoryEntity(name = "Removable", type = CategoryType.EXPENSE).toDomainModel())
//        val id = insertResult.getOrNull()!!
//
//        // Delete
//        val deleteResult = repository.deleteCategory(id)
//        assertTrue(deleteResult.isSuccess)
//
//        // Verify
//        val dbCategory = categoryDao.getCategoryById(id)
//        assertNull(dbCategory)
//    }
//
//    @Test
//    fun getActiveCategories() = runBlocking {
//        // Insert
//        repository.addCategory(CategoryEntity(name = "Cat A", type = CategoryType.EXPENSE).toDomainModel())
//        repository.addCategory(CategoryEntity(name = "Cat B", type = CategoryType.INCOME).toDomainModel())
//
//        // Because it's a Flow, let's collect it once
//        val flow = repository.getActiveCategories()
//        val job = launch {
//            flow.collect { categoryList ->
//                assertTrue(categoryList.size >= 2)
//                // You can assert on names, types, etc.
//                cancel() // Stop collecting after verifying once
//            }
//        }
//        job.join()
//    }
}
