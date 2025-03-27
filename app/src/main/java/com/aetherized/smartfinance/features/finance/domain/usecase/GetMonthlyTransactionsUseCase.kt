package com.aetherized.smartfinance.features.finance.domain.usecase

import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject


class GetMonthlyTransactionsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    operator fun invoke (
        yearMonth: YearMonth,
        limit: Int = 50,
        offset: Int = 0
    ): Flow<List<Transaction>> {
        return repository.getMonthlyActiveTransactions(yearMonth, limit, offset)
    }
}
