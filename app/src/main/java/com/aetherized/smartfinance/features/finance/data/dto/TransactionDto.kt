package com.aetherized.smartfinance.features.finance.data.dto

import java.time.LocalDateTime

data class TransactionDto(
    val id: Long = 0L,
    val categoryId: Long,
    val amount: Int,
    val note: String? = null,
    val timestamp: String = LocalDateTime.now().toString(),
    val isDeleted: Boolean = false,
    val lastModified: String = LocalDateTime.now().toString()
)
