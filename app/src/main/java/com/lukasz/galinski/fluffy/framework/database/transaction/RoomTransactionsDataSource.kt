package com.lukasz.galinski.fluffy.framework.database.transaction

import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.domain.repository.TransactionsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoomTransactionsDataSource @Inject constructor(
    private val transactionsDao: TransactionsDao
) : TransactionsDataSource {

    override fun getTransactions(
        userId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> {
        return flow {
            emit(transactionsDao.getMonthTransactions(userId, startDate, endDate)
                .map { it.toTransaction() }
            )
        }
    }

    override fun addTransaction(transaction: Transaction): Flow<Long> {
        return flow {
            emit(transactionsDao.addNewTransaction(TransactionEntity.fromTransaction(transaction)))
        }
    }
}