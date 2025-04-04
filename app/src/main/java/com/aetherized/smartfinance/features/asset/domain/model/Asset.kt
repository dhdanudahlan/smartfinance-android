package com.aetherized.smartfinance.features.asset.domain.model

import androidx.room.PrimaryKey
import java.time.LocalDateTime

data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: AssetType,
    val balance: Double,
    val income: Double,
    val expense: Double,
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime = LocalDateTime.now(),
    val isNew: Boolean = false
)

enum class AssetType {
    CASH, ACCOUNTS
}
