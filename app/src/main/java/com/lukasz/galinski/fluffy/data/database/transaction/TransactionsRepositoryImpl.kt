package com.lukasz.galinski.fluffy.data.database.transaction

import com.lukasz.galinski.fluffy.data.model.TransactionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsDao: TransactionsDao
) : TransactionsRepository {

    override fun getTransactions(
        userId: Long,
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionModel>> {
        return flow {
            val persons = transactionsDao.getMonthTransactions(userId, startDate, endDate)
            emit(persons)
        }
    }

    override fun addTransaction(transactionModel: TransactionModel): Flow<Long>{
        return flow {
            val transactionId = transactionsDao.addNewTransaction(transactionModel)
            emit(transactionId)
        }
    }
}