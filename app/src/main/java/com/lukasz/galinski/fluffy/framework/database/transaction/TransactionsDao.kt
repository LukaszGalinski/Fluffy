package com.lukasz.galinski.fluffy.framework.database.transaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionsDao {
    @Insert
    fun addNewTransaction(transactionEntity: TransactionEntity): Long

    @Query("SELECT * FROM TransactionsTable WHERE userId LIKE :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getMonthTransactions(userId: Long, startDate: Long, endDate: Long): List<TransactionEntity>

    @Query("DELETE FROM TransactionsTable WHERE transactionId LIKE:transactionId")
    fun deleteTransaction(transactionId: Long)
}