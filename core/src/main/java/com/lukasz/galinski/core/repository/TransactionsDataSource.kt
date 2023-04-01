package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsDataSource {
    fun addTransaction(transaction: Transaction): Flow<Long>
    fun getTransactions(userId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>>
}