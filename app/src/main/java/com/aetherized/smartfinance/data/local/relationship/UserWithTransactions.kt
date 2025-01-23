package com.aetherized.smartfinance.data.local.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.aetherized.smartfinance.data.local.entity.TransactionEntity
import com.aetherized.smartfinance.data.local.entity.UserEntity


data class UserWithTransactions(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val transactions: List<TransactionEntity>
)

