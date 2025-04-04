package com.aetherized.smartfinance.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aetherized.smartfinance.core.utils.SyncStatus
import com.aetherized.smartfinance.features.asset.domain.model.AssetType

@Entity(
    tableName = "accounts",
    indices = [
        Index(value = ["last_modified"]),
        Index(value = ["is_deleted"]),
        Index(value = ["sync_status"])
    ]
)
data class AssetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: AssetType,
    val balance: Double,
    val income: Double,
    val expense: Double,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "last_modified")
    val lastModified: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING
)
