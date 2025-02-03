package com.aetherized.smartfinance.features.finance.domain.model

import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.features.finance.data.dto.TransactionDto
import java.time.LocalDateTime
import java.time.ZoneId

data class Transaction(
    val id: Long = 0L,
    val categoryId: Long,
    val amount: Double,
    val note: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime = LocalDateTime.now()
) {
    fun toEntity(): TransactionEntity {
        if (id != (-1).toLong()) {
            return TransactionEntity(
                id = this.id,
                categoryId = this.categoryId,
                amount = this.amount,
                note = this.note,
                timestamp = this.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                isDeleted = this.isDeleted,
                lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        } else {
            return TransactionEntity(
                categoryId = this.categoryId,
                amount = this.amount,
                note = this.note,
                timestamp = this.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                isDeleted = this.isDeleted,
                lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        }
    }

    fun toDTO(): TransactionDto {
        return TransactionDto(
            id = this.id,
            categoryId = this.categoryId,
            amount = this.amount,
            note = this.note,
            timestamp = this.timestamp.toString(),
            isDeleted = this.isDeleted,
            lastModified = this.lastModified.toString()
        )
    }
}
