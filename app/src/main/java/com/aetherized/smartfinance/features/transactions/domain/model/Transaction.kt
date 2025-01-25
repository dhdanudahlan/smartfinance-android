package com.aetherized.smartfinance.features.transactions.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.features.transactions.data.dto.TransactionDto
import java.time.LocalDateTime
import java.time.ZoneId

data class Transaction(
    val id: Long = 0L,
    val categoryId: Long,
    val amount: Double,
    val note: String? = null,
    val timestamp: LocalDateTime,
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime
) {
    fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = this.id,
            categoryId = this.categoryId,
            amount = this.amount,
            note = this.note,
            timestamp = this.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            isDeleted = this.isDeleted,
            lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
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
