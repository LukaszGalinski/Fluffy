package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.Transaction

interface TransactionsDataSource {
    suspend fun addTransaction(transaction: Transaction): Long
    suspend fun getTransactions(userId: Long, startDate: Long, endDate: Long): List<Transaction>
}