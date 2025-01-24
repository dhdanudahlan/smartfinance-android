package com.aetherized.smartfinance.core.database.entity

import com.aetherized.smartfinance.core.utils.CategoryType
import org.junit.Assert.assertThrows
import org.junit.Test

class TransactionEntityTest {

    @Test
    fun `creating transaction with amount not greater than zero throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            val category = CategoryEntity(name = "Food", type = CategoryType.EXPENSE)
            TransactionEntity(categoryId = category.id, amount = 0.0)
        }
    }

    @Test
    fun `creating transaction with valid amount succeeds`() {
        val category = CategoryEntity(name = "Food", type = CategoryType.EXPENSE)
        val transaction = TransactionEntity(categoryId = category.id, amount = 1000.0)
        assert(transaction.amount >= 0)
    }
}
