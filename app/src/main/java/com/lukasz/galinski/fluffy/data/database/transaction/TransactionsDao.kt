package com.lukasz.galinski.fluffy.data.database.transaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lukasz.galinski.fluffy.data.model.TransactionModel

@Dao
interface TransactionsDao {
    @Insert
    fun addNewTransaction(transactionModel: TransactionModel): Long

    @Query("SELECT * FROM TransactionsTable WHERE userId LIKE :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getMonthTransactions(userId: Long, startDate: Long, endDate: Long): List<TransactionModel>

    @Query("DELETE FROM TransactionsTable WHERE transactionId LIKE:transactionId")
    fun deleteTransaction(transactionId: Long)
}