package com.aetherized.smartfinance.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aetherized.smartfinance.features.finance.domain.model.Account

@Entity(
    tableName = "accounts",
    indices = [
        Index(value = ["last_modified"]),
        Index(value = ["is_deleted"]),
        Index(value = ["sync_status"])
    ]
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: String,
    val balance: Double,
    val income: Double,
    val expense: Double
) {
    fun toAccount(): Account = Account(
        id = this.id,
        name = this.name,
        type = this.type,
        balance = this.balance,
        income = this.income,
        expense = this.expense
    )
}
