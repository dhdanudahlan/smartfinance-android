package com.aetherized.smartfinance.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val type: String,
    val balance: Double,
    val income: Double,
    val expense: Double
)
