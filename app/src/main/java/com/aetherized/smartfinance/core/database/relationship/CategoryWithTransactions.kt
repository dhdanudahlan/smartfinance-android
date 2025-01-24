package com.aetherized.smartfinance.core.database.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.database.entity.TransactionEntity


data class CategoryWithTransactions(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val transactions: List<TransactionEntity>
)

