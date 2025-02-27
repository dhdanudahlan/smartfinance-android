package com.aetherized.smartfinance.features.finance.presentation

import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.Transaction

sealed class TransactionDetailsUIState {
    data object Loading : TransactionDetailsUIState()
    data class Success(
        val transaction: Transaction,
        val category: Category
    ) : TransactionDetailsUIState()
    data class Error(val message: String) : TransactionDetailsUIState()
}
