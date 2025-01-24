package com.aetherized.smartfinance.core.database.entity

import com.aetherized.smartfinance.core.utils.CategoryType
import org.junit.Assert.assertThrows
import org.junit.Test

class CategoryEntityTest {

    @Test
    fun `creating category with blank name throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            CategoryEntity(name = "", type = CategoryType.EXPENSE)
        }
    }

    @Test
    fun `creating category with valid name succeeds`() {
        val category = CategoryEntity(name = "Food", type = CategoryType.EXPENSE)
        assert(category.name == "Food")
    }
}
