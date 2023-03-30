package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.Transaction

interface TransactionsDataSource {
    fun addTransaction(transaction: Transaction): Long
    fun getTransactions(userId: Long, startDate: Long, endDate: Long): List<Transaction>
}