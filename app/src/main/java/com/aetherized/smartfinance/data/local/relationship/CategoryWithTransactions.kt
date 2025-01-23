package com.aetherized.smartfinance.data.local.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.aetherized.smartfinance.data.local.entity.CategoryEntity
import com.aetherized.smartfinance.data.local.entity.TransactionEntity
import com.aetherized.smartfinance.data.local.entity.UserEntity


data class CategoryWithTransactions(
    @Embedded val user: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val transactions: List<TransactionEntity>
)

