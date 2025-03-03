package com.aetherized.smartfinance.core.usecase

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.CategoryType
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import com.aetherized.smartfinance.features.finance.domain.usecase.GetCategoriesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeFinanceRepository : FinanceRepository {
    override fun getActiveCategories(limit: Int, offset: Int) =
        flowOf(
            listOf(
                Category(1, "Food", CategoryType.EXPENSE, "#FF0000", "food_icon", false),
                Category(2, "Salary", CategoryType.INCOME, "#00FF00", "salary_icon", false)
            )
        )
    override fun getCategoriesByType(type: CategoryType, limit: Int, offset: Int) =
        flowOf(listOf(Category(1, "Food", CategoryType.EXPENSE, "#FF0000", "food_icon", false)))

    override fun getCategoryById(id: Long): Flow<Category> {
        TODO("Not yet implemented")
    }

    // Implement or stub the rest of the methods as needed.
    override suspend fun saveCategory(category: Category) = TODO()
    override suspend fun deleteCategory(id: Long) = TODO()
    override fun getActiveTransactions(limit: Int, offset: Int) = flowOf(emptyList<Transaction>())
    override fun getTransactionsByCategory(categoryId: Long, limit: Int, offset: Int) = flowOf(emptyList<Transaction>())
    override fun getTransactionsByType(categoryType: CategoryType, limit: Int, offset: Int) = flowOf(emptyList<Transaction>())
    override fun getTransactionById(id: Long): Flow<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTransaction(transaction: Transaction) = TODO()
    override suspend fun deleteTransaction(id: Long) = TODO()
    override suspend fun syncData() = TODO()
}

class GetCategoriesUseCaseTest {

    private val fakeRepository = FakeFinanceRepository()
    private val getCategoriesUseCase = GetCategoriesUseCase(fakeRepository)

    @Test
    fun `invoke without type returns all active categories`() = runBlocking {
        val categories = getCategoriesUseCase.invoke(limit = 50, offset = 0).first()
        // Expecting 2 categories from fake repository.
        assertEquals(2, categories.size)
    }

    @Test
    fun `invoke with type returns filtered categories`() = runBlocking {
        val categories = getCategoriesUseCase.invoke(categoryType = CategoryType.EXPENSE, limit = 50, offset = 0).first()
        // Expecting 1 category from fake repository.
        assertEquals(1, categories.size)
    }
}
