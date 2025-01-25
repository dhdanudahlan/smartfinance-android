package com.aetherized.smartfinance.features.transactions.data.dto

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aetherized.smartfinance.core.utils.TransactionValidator
import com.aetherized.smartfinance.features.transactions.domain.model.Transaction
import java.time.LocalDateTime

data class TransactionDto(
    val id: Long = 0L,
    val categoryId: Long,
    val amount: Double,
    val note: String? = null,
    val timestamp: String,
    val isDeleted: Boolean = false,
    val lastModified: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
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
