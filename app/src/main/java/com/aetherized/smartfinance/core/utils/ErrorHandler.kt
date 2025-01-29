package com.aetherized.smartfinance.core.utils

object ErrorHandler {
    fun handleException(e: Exception): String {
        return when (e) {
            is IllegalArgumentException -> e.message ?: "Invalid input provided."
            is NullPointerException -> "Unexpected null value encountered."
            else -> "An unexpected error occurred."
        }
    }
}
