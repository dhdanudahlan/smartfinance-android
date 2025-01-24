package com.aetherized.smartfinance.data.local.dao

import com.aetherized.smartfinance.data.local.entity.CategoryEntity
import com.aetherized.smartfinance.data.local.entity.TransactionEntity
import com.aetherized.smartfinance.utils.CategoryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TransactionDaoTest : BaseDaoTest() {

    private lateinit var transactionDao: TransactionDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun initDao() {
        transactionDao = database.transactionDao()
        categoryDao = database.categoryDao()
    }

    @Test
    fun insertTransaction_success() = runBlockingTest {
        // Insert a category first
        val categoryId = insertSampleCategory()
        // Then insert a transaction referencing the categoryId
        val transaction = TransactionEntity(categoryId = categoryId, amount = 100.0)
        val id = transactionDao.insertTransaction(transaction)
        assertTrue(id > 0)
    }

    @Test
    fun getTransactionsByCategory_returnsCorrectData() = runBlockingTest {
        // Insert a category first
        val categoryId = insertSampleCategory()
        // Then insert a transaction referencing the categoryId
        val transaction = TransactionEntity(categoryId = categoryId, amount = 50.0)
        transactionDao.insertTransaction(transaction)

        val transactions = transactionDao.getTransactionsByCategory(1).first()
        assertEquals(1, transactions.size)
        assertEquals(50.0, transactions[0].amount, 0.001)
    }

    suspend fun insertSampleCategory(): Long {
        return categoryDao.insertCategory(
            CategoryEntity(name = "Sample Category", type = CategoryType.INCOME)
        )
    }
}
