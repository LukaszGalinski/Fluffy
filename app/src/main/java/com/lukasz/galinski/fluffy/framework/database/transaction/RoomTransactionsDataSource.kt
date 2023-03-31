package com.lukasz.galinski.fluffy.framework.database.transaction

import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.repository.TransactionsDataSource
import javax.inject.Inject

class RoomTransactionsDataSource @Inject constructor(
    private val transactionsDao: TransactionsDao
) : TransactionsDataSource {

    override suspend fun getTransactions(
        userId: Long,
        startDate: Long,
        endDate: Long
    ): List<Transaction> {
        return transactionsDao.getMonthTransactions(userId, startDate, endDate)
            .map { it.toTransaction() }
    }

    override suspend fun addTransaction(transaction: Transaction): Long {
        return transactionsDao.addNewTransaction(TransactionEntity.fromTransaction(transaction))
    }
}