package com.aetherized.smartfinance.core.utils

import com.aetherized.smartfinance.core.database.model.TransactionEntity

class TransactionValidator {
    companion object {
        fun validateAmount(transaction: TransactionEntity): Boolean {
            require(transaction.amount > 0) { "Transaction amount must be greater than zero." }
            return true
        }
    }
}

