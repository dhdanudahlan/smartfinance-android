package com.aetherized.smartfinance.features.records.data.dto

import com.aetherized.smartfinance.features.records.domain.model.Transaction
import java.time.LocalDateTime

data class TransactionDto(
    val id: Long = 0L,
    val categoryId: Long,
    val amount: Double,
    val note: String? = null,
    val timestamp: String = LocalDateTime.now().toString(),
    val isDeleted: Boolean = false,
    val lastModified: String = LocalDateTime.now().toString()
) {
    fun toDomainModel(): Transaction {
        return Transaction(
            id = this.id,
            categoryId = this.categoryId,
            amount = this.amount,
            note = this.note,
            timestamp = LocalDateTime.parse(this.timestamp),
            isDeleted = this.isDeleted,
            lastModified = LocalDateTime.parse(this.lastModified)
        )
    }
}
