package com.aetherized.smartfinance.features.finance.presentation.component

import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale


// FUNCTIONS //
fun getDatesOfTheYearMonth(yearMonth: YearMonth = YearMonth.now()): List<LocalDate> {
    // Get how many days in the current month
    val daysInMonth = yearMonth.lengthOfMonth()

    // Build a list of LocalDate for each day in the current month
    return (1..daysInMonth).map { day ->
        LocalDate.of(yearMonth.year, yearMonth.month, day)
    }
}

// FUNCTION EXTENSIONS //
fun Int.toNumericalString(): String {
    return if ((this > 0) and (this < 10) ) {
        "0$this"
    } else {
        "$this"
    }
}

fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
