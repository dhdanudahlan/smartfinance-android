package com.aetherized.smartfinance.features.finance.domain.model

import androidx.room.PrimaryKey

data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: String,
    val balance: Double,
    val income: Double,
    val expense: Double
)
