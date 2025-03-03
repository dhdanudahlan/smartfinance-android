package com.aetherized.smartfinance.features.finance.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0L,
    val categoryId: Long,
    val accountId: Long,
    val amount: Double,
    val note: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false,
    val lastModified: LocalDateTime = LocalDateTime.now()
)
