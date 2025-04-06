package com.aetherized.smartfinance.core.model.data

import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0L,
    val categoryId: Long,
    val assetId: Long,
    val amount: Double,
    val label: String? = null,
    val note: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime = LocalDateTime.now(),
    val isNew: Boolean = false
)
