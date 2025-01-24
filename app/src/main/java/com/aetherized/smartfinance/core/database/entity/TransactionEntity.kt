package com.aetherized.smartfinance.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

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
        Index(value = ["is_deleted"])
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(defaultValue = "0") // Default value for categoryId
    val categoryId: Long, // FK to CategoryEntity
    val amount: Double,
    val note: String? = null,
    val timestamp: Long = currentTimestamp,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "last_modified")
    val lastModified: Long = currentTimestamp,
) {
    companion object {
        val currentTimestamp: Long
            get() = System.currentTimeMillis()
    }
}
