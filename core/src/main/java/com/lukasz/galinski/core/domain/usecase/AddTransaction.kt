package com.lukasz.galinski.core.domain.usecase

import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.domain.AddTransactionResult
import com.lukasz.galinski.core.domain.DateTimeOperations
import com.lukasz.galinski.core.domain.repository.TransactionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class AddTransaction(
    private val transactionsRepository: TransactionsRepository,
    private val dateTimeOperations: DateTimeOperations
) {
    operator fun invoke(transaction: Transaction): Flow<AddTransactionResult<Any>> {
        return flow {
            transactionsRepository.addTransaction(transaction).catch {
                emit(AddTransactionResult.Error(it.message.toString()))
            }.onEach {
                if (newTransactionInTimeRange(transaction.date)) {
                    emit(AddTransactionResult.SuccessInTimeRange)
                } else {
                    emit(AddTransactionResult.Success)
                }
            }.collect()
        }
    }

    private fun newTransactionInTimeRange(date: Long) =
        (dateTimeOperations.getStartMonthDate() <= date) && (date <= dateTimeOperations.getEndMonthDate())
}