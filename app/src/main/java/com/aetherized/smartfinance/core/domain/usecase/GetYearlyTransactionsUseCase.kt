package com.aetherized.smartfinance.core.domain.usecase

import com.aetherized.smartfinance.core.model.data.Transaction
import com.aetherized.smartfinance.core.data.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject


class GetYearlyTransactionsUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    operator fun invoke (
        yearMonth: YearMonth,
        limit: Int = 50,
        offset: Int = 0
    ): Flow<List<Transaction>> {
        return repository.getYearlyActiveTransactions(yearMonth, limit, offset)
    }
}
