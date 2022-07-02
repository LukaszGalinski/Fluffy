package com.lukasz.galinski.fluffy.repository.database.transaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lukasz.galinski.fluffy.model.TransactionModel

@Dao
interface TransactionsDao {

    @Insert
    fun addNewTransaction(transactionModel: TransactionModel): Long

    @Query("SELECT * FROM TransactionModel WHERE userId LIKE :userId AND date BETWEEN :startDate AND :endDate")
    fun getMonthTransactions(userId: Long, startDate: Long, endDate: Long): List<TransactionModel>


    @Query("DELETE FROM TransactionModel WHERE transactionId LIKE:transactionId")
    fun deleteTransaction(transactionId: Long)
}