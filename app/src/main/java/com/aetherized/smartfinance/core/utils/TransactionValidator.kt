package com.aetherized.smartfinance.core.utils

import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import java.text.NumberFormat
import java.util.Locale

class TransactionValidator {
    companion object {
        fun validateAmount(transaction: TransactionEntity): Boolean {
            require(transaction.amount > 0) { "Transaction amount must be greater than zero." }
            return true
        }
    }
}

