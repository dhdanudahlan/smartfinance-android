package com.aetherized.smartfinance.core.database.entity

import com.aetherized.smartfinance.core.utils.CategoryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CategoryEntityTest {

//    @Test
//    fun `should throw IllegalArgumentException when name is blank`() {
//        val exception = assertThrows(IllegalArgumentException::class.java) {
//            CategoryEntity(name = "", type = CategoryType.EXPENSE)
//        }
//        assertEquals("Category name cannot be blank.", exception.message)
//    }

    @Test
    fun `should create category with valid name and type`() {
        val category = CategoryEntity(name = "Food", type = CategoryType.EXPENSE)
        assertEquals("Food", category.name)
        assertEquals(CategoryType.EXPENSE, category.type)
    }

//    @Test
//    fun `should throw IllegalArgumentException when name exceeds maximum length`() {
//        val longName = "a".repeat(256) // Assuming max length is 255
//        val exception = assertThrows(IllegalArgumentException::class.java) {
//            CategoryEntity(name = longName, type = CategoryType.EXPENSE)
//        }
//        assertEquals("Category name exceeds maximum length.", exception.message)
//    }
}
