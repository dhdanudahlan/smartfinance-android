package com.aetherized.smartfinance.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aetherized.smartfinance.core.database.SmartFinanceDatabase
import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.dao.TransactionDao
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.core.utils.CategoryType
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
class TransactionDaoInstrumentedTest {

    private lateinit var db: SmartFinanceDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            SmartFinanceDatabase::class.java
        ).allowMainThreadQueries().build()
        categoryDao = db.categoryDao()
        transactionDao = db.transactionDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetTransaction() = runBlocking {
        // Given
        val category = CategoryEntity(
            name = "Groceries",
            type = CategoryType.EXPENSE,
            color = "#FF0000"
        )
        val categoryId = categoryDao.insertCategory(category)

        val transaction = TransactionEntity(
            categoryId = categoryId,
            amount = 100.0
        )

        // When
        val insertedId = transactionDao.insertTransaction(transaction)

        // Then
        val loadedTransaction = transactionDao.getTransactionById(insertedId)
        assertNotNull(loadedTransaction)
        assertEquals(100.0, loadedTransaction?.amount)
        assertEquals(categoryId, loadedTransaction?.categoryId)
    }

    @Test
    fun updateTransaction() = runBlocking {
        // Insert
        val category = CategoryEntity(
            name = "Groceries",
            type = CategoryType.EXPENSE,
            color = "#FF0000"
        )
        val categoryId = categoryDao.insertCategory(category)

        val transaction = TransactionEntity(
            categoryId = categoryId,
            amount = 100.0
        )
        val insertedId = transactionDao.insertTransaction(transaction)

        // Update
        val updatedTransaction = TransactionEntity(
            id = insertedId,
            categoryId = categoryId,
            amount = 150.0
        )
        transactionDao.updateTransaction(updatedTransaction)

        // Verify
        val loadedTransaction = transactionDao.getTransactionById(insertedId)
        assertEquals(150.0, loadedTransaction?.amount)
    }

    @Test
    fun deleteCategory() = runBlocking {
        // Insert
        val category = CategoryEntity(
            name = "Groceries",
            type = CategoryType.EXPENSE,
            color = "#FF0000"
        )
        val categoryId = categoryDao.insertCategory(category)

        val transaction = TransactionEntity(
            categoryId = categoryId,
            amount = 100.0
        )
        val insertedId = transactionDao.insertTransaction(transaction)

        // Delete
        transactionDao.deleteTransactionById(insertedId)

        // Verify
        val loadedTransaction = transactionDao.getTransactionById(insertedId)
        assertNull(loadedTransaction)
    }

    @Test
    fun getAllActiveTransactions() = runBlocking {
        // Insert a couple of active categories

        val categoryIdFirst = categoryDao.insertCategory(
            CategoryEntity(
                name = "Groceries",
                type = CategoryType.EXPENSE,
                color = "#FF0000"
            )
        )
        val categoryIdSecond = categoryDao.insertCategory(
            CategoryEntity(
                name = "Food",
                type = CategoryType.EXPENSE,
                color = "#FF0000"
            )
        )
        val categoryIdThird = categoryDao.insertCategory(
            CategoryEntity(
                name = "Health",
                type = CategoryType.EXPENSE,
                color = "#FF0000"
            )
        )

        transactionDao.insertTransaction(TransactionEntity(
            categoryId = categoryIdFirst,
            amount = 100.0
        ))
        transactionDao.insertTransaction(TransactionEntity(
            categoryId = categoryIdSecond,
            amount = 150.0
        ))
        // Insert a deleted category
        transactionDao.insertTransaction(TransactionEntity(
            categoryId = categoryIdThird,
            amount = 200.0,
            isDeleted = true
        ))

        // Flow usage: collect the flow and test the emitted values
        val transactionFlow = transactionDao.getAllActiveTransactions()
        val job = launch {
            transactionFlow.collect { transaction ->
                // We expect 2 active categories
                assertEquals(2, transaction.size)
                // Optional: cancel collection after verification
                cancel()
            }
        }
        job.join()
    }
}
