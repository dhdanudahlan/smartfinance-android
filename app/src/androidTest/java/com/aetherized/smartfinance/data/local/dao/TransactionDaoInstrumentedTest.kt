package com.aetherized.smartfinance.data.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionDaoInstrumentedTest {

//    private lateinit var db: SmartFinanceDatabase
//    private lateinit var categoryDao: CategoryDao
//    private lateinit var transactionDao: TransactionDao
//
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(
//            context,
//            SmartFinanceDatabase::class.java
//        ).allowMainThreadQueries().build()
//        categoryDao = db.categoryDao()
//        transactionDao = db.transactionDao()
//    }
//
//    @After
//    fun closeDb() {
//        db.close()
//    }
//
//    @Test
//    fun insertAndGetTransaction() = runBlocking {
//        // Given
//        val category = CategoryEntity(
//            name = "Groceries",
//            type = CategoryType.EXPENSE,
//            color = "#FF0000"
//        )
//        val categoryId = categoryDao.upsertCategory(category)
//
//        val transaction = TransactionEntity(
//            categoryId = categoryId,
//            amount = 100.0
//        )
//
//        // When
//        val insertedId = transactionDao.upsertTransaction(transaction)
//
//        // Then
//        val loadedTransactions = transactionDao.getTransactionsByCategoryId(insertedId)
//        var loadedTransaction: Transaction? = null
//        loadedTransactions.collectLatest { list -> loadedTransaction =
//            list.find { it.categoryId == insertedId }?.toDomainModel()
//        }
//
//        assertNotNull(loadedTransaction)
//        assertEquals(100.0, loadedTransaction?.amount)
//        assertEquals(categoryId, loadedTransaction?.categoryId)
//    }
//
//    @Test
//    fun updateTransaction() = runBlocking {
//        // Insert
//        val category = CategoryEntity(
//            name = "Groceries",
//            type = CategoryType.EXPENSE,
//            color = "#FF0000"
//        )
//        val categoryId = categoryDao.upsertCategory(category)
//
//        val transaction = TransactionEntity(
//            categoryId = categoryId,
//            amount = 100.0
//        )
//        val insertedId = transactionDao.upsertTransaction(transaction)
//
//        // Update
//        val updatedTransaction = TransactionEntity(
//            id = insertedId,
//            categoryId = categoryId,
//            amount = 150.0
//        )
//        transactionDao.upsertTransaction(updatedTransaction)
//
//        // Verify
//        val loadedTransactions = transactionDao.getTransactionsByCategoryId(insertedId)
//        var loadedTransaction: Transaction? = null
//        loadedTransactions.collectLatest { list -> loadedTransaction =
//            list.find { it.categoryId == insertedId }?.toDomainModel()
//        }
//        assertEquals(150.0, loadedTransaction?.amount)
//    }
//
//    @Test
//    fun deleteCategory() = runBlocking {
//        // Insert
//        val category = CategoryEntity(
//            name = "Groceries",
//            type = CategoryType.EXPENSE,
//            color = "#FF0000"
//        )
//        val categoryId = categoryDao.upsertCategory(category)
//
//        val transaction = TransactionEntity(
//            categoryId = categoryId,
//            amount = 100.0
//        )
//        val insertedId = transactionDao.upsertTransaction(transaction)
//
//        // Delete
//        transactionDao.softDeleteTransactionById(insertedId)
//
//        // Verify
//        val loadedTransaction = transactionDao.getTransactionsByCategoryId(insertedId)
//        assertNull(loadedTransaction)
//    }
//
//    @Test
//    fun getAllActiveTransactions() = runBlocking {
//        // Insert a couple of active categories
//
//        val categoryIdFirst = categoryDao.upsertCategory(
//            CategoryEntity(
//                name = "Groceries",
//                type = CategoryType.EXPENSE,
//                color = "#FF0000"
//            )
//        )
//        val categoryIdSecond = categoryDao.upsertCategory(
//            CategoryEntity(
//                name = "Food",
//                type = CategoryType.EXPENSE,
//                color = "#FF0000"
//            )
//        )
//        val categoryIdThird = categoryDao.upsertCategory(
//            CategoryEntity(
//                name = "Health",
//                type = CategoryType.EXPENSE,
//                color = "#FF0000"
//            )
//        )
//
//        transactionDao.upsertTransaction(TransactionEntity(
//            categoryId = categoryIdFirst,
//            amount = 100.0
//        ))
//        transactionDao.upsertTransaction(TransactionEntity(
//            categoryId = categoryIdSecond,
//            amount = 150.0
//        ))
//        // Insert a deleted category
//        transactionDao.upsertTransaction(TransactionEntity(
//            categoryId = categoryIdThird,
//            amount = 200.0,
//            isDeleted = true
//        ))
//
//        // Flow usage: collect the flow and test the emitted values
//        val transactionFlow = transactionDao.getAllActiveTransactions()
//        val job = launch {
//            transactionFlow.collect { transaction ->
//                // We expect 2 active categories
//                assertEquals(2, transaction.size)
//                // Optional: cancel collection after verification
//                cancel()
//            }
//        }
//        job.join()
//    }
}
