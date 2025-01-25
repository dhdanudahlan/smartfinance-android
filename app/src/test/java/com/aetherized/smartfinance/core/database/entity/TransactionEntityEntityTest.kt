package com.aetherized.smartfinance.core.database.entity

import com.aetherized.smartfinance.core.utils.CategoryType
import org.junit.Test

class TransactionEntityEntityTest {

//    @Test
//    fun `should throw IllegalArgumentException when amount is greater than zero`() {
//        val exception = assertThrows(IllegalArgumentException::class.java) {
//            val category = CategoryEntity(name = "Food", type = CategoryType.EXPENSE)
//            TransactionEntity(categoryId = category.id, amount = 0.0)
//        }
//        assertEquals("Transaction amount must be greater than zero.", exception.message)
//    }


    @Test
    fun `should create transaction with valid amount`() {
        val category = CategoryEntity(name = "Food", type = CategoryType.EXPENSE)
        val transaction = TransactionEntity(categoryId = category.id, amount = 1000.0)
        assert(transaction.amount >= 0)
    }

}
