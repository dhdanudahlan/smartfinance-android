package com.aetherized.smartfinance.features.finance.domain.model

import java.time.LocalDate

data class DailySummary(
    val date: LocalDate,
    val transactions: List<Transaction>,
    val income: Double,   // or Double if needed
    val expense: Double
)
