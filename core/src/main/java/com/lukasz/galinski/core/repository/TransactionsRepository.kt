package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.Transaction

class TransactionsRepository(private val dataSource: TransactionsDataSource) {
    suspend fun addTransaction(transaction: Transaction) = dataSource.addTransaction(transaction)
    suspend fun getTransactions(userId: Long, startDate: Long, endDate: Long) =
        dataSource.getTransactions(userId, startDate, endDate)
}