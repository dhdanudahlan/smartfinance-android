package com.aetherized.smartfinance.data.local.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.aetherized.smartfinance.data.local.entity.CategoryEntity
import com.aetherized.smartfinance.data.local.entity.TransactionEntity


data class CategoryWithTransactions(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val transactions: List<TransactionEntity>
)

