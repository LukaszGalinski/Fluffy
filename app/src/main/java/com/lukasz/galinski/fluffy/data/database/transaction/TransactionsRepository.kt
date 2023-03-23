package com.lukasz.galinski.fluffy.data.database.transaction

import com.lukasz.galinski.fluffy.data.model.TransactionModel
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    fun addTransaction(transactionModel: TransactionModel): Flow<Long>
    fun getTransactions(userId: Long, startDate: Long, endDate: Long): Flow<List<TransactionModel>>
}