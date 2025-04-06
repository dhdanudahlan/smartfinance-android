package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.model.data.Transaction
import com.aetherized.smartfinance.core.data.repository.FinanceRepository
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
