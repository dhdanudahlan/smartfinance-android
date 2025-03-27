package com.aetherized.smartfinance.features.finance.domain.model

import java.time.LocalDate
import java.time.YearMonth

data class DailySummary(
    val date: LocalDate,
    val transactions: List<Transaction>,
    val income: Double,
    val expense: Double
)

data class MonthlySummary(
    val yearMonth: YearMonth,
    val transactions: List<Transaction>,
    val income: Double,
    val expense: Double
)
