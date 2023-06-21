package com.lukasz.galinski.core.domain.repository

import com.lukasz.galinski.core.data.Transaction

class TransactionsRepository(private val dataSource: TransactionsDataSource) {
    fun addTransaction(transaction: Transaction) = dataSource.addTransaction(transaction)
    fun getTransactions(userId: Long, startDate: Long, endDate: Long) =
        dataSource.getTransactions(userId, startDate, endDate)
}