package com.aetherized.smartfinance.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aetherized.smartfinance.core.utils.SyncStatus
import com.aetherized.smartfinance.features.records.domain.model.Transaction
import java.time.Instant
import java.time.ZoneId

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_DEFAULT
        )
    ],
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["last_modified"]),
        Index(value = ["is_deleted"]),
        Index(value = ["sync_status"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    val categoryId: Long, // Must reference an existing category or a default one (e.g., Uncategorized)
    val amount: Double,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "last_modified")
    val lastModified: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING
) {
    fun toDomainModel(): Transaction {
        return Transaction(
            id = id,
            categoryId = categoryId,
            amount = amount,
            note = note,
            timestamp = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime(),
            isDeleted = isDeleted,
            lastModified = Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault()).toLocalDateTime()
        )
    }
}
