package com.aetherized.smartfinance.data.local.dao

import com.aetherized.smartfinance.core.database.dao.CategoryDao
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.utils.CategoryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoryDaoTest : BaseDaoTest() {
    private lateinit var categoryDao: CategoryDao

    @Before
    fun initDao() {
        categoryDao = database.categoryDao()
    }

    @Test
    fun insertCategory_success() = runBlockingTest {
        val category = CategoryEntity(name = "food", type = CategoryType.EXPENSE)
        val id = categoryDao.insertCategory(category)
        assertTrue(id > 0) // Ensure valid ID is returned
    }

    @Test
    fun getAllCategories_returnsCorrectCount() = runBlockingTest {
        categoryDao.insertCategory(CategoryEntity(name = "Groceries", type = CategoryType.EXPENSE))
        categoryDao.insertCategory(CategoryEntity(name = "Salary", type = CategoryType.INCOME))

        val categories = categoryDao.getAllCategories().first()
        assertEquals(2, categories.size)
    }

    @Test
    fun updateCategory_updatesCorrectly() = runBlockingTest {
        val id = categoryDao.insertCategory(CategoryEntity(name = "Travel", type = CategoryType.EXPENSE))
        val updatedCategory = CategoryEntity(id = id, name = "Travel & Leisure", type = CategoryType.EXPENSE)
        categoryDao.updateCategory(updatedCategory)

        val categories = categoryDao.getAllCategories().first()
        assertEquals("Travel & Leisure", categories[0].name)
    }

}
