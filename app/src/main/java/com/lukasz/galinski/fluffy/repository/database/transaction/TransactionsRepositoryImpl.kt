package com.lukasz.galinski.fluffy.repository.database.transaction

import com.lukasz.galinski.fluffy.model.TransactionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val transactionsDao: TransactionsDao
): TransactionsRepository {

    override fun getTransactions(userId: Long, startDate: Long, endDate: Long): Flow<List<TransactionModel>> {
        return flow {
            val persons = transactionsDao.getMonthTransactions(userId, startDate, endDate)
            emit(persons)
        }
    }

    override fun addTransaction(transactionModel: TransactionModel){
        transactionsDao.addNewTransaction(transactionModel)
    }
}