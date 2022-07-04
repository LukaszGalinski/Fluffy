package com.lukasz.galinski.fluffy.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.model.UserModel

@Dao
interface DatabaseDao {

    @Insert
    fun addNewUser(user: UserModel): Long

    @Query("SELECT userId FROM usersTable WHERE userEmail LIKE:email AND password LIKE:password")
    fun loginUser(email: String, password: String): Long

    @Query("SELECT * FROM usersTable")
    fun getAllUsers(): List<UserModel>

    @Query("SELECT * FROM usersTable WHERE userId LIKE:userId")
    fun getUser(userId: Long): UserModel

    @Query("DELETE FROM usersTable WHERE userId LIKE:userId")
    fun deleteUser(userId: Long)


    @Insert
    fun addNewTransaction(transactionModel: TransactionModel): Long

    @Query("SELECT * FROM TransactionsTable WHERE userId LIKE :userId AND date BETWEEN :startDate AND :endDate")
    fun getMonthTransactions(userId: Long, startDate: Long, endDate: Long): List<TransactionModel>

    @Query("DELETE FROM TransactionsTable WHERE transactionId LIKE:transactionId")
    fun deleteTransaction(transactionId: Long)
}