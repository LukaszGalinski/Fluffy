package com.lukasz.galinski.fluffy

import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionEntity
import com.lukasz.galinski.fluffy.framework.database.user.UserEntity

object TestUtilities {
    fun createTestUsers(testUsersLastIndex: Int): ArrayList<UserEntity> {
        val usersList = arrayListOf<UserEntity>()
        for (i in 0..testUsersLastIndex) {
            usersList.add(
                UserEntity(
                    "George",
                    "emailExample$i@test.com",
                    "test",
                    "0000",
                    1
                )
            )
        }
        return usersList
    }

    fun createTestTransactions(testUsersLastIndex: Int): ArrayList<TransactionEntity> {
        val transactionList = arrayListOf<TransactionEntity>()
        for (i in 0..testUsersLastIndex) {
            transactionList.add(
                TransactionEntity(
                    "dummyTransaction",
                    0,
                    "food",
                    483.23,
                    "Dummy Description",
                    "income",
                    1
                )
            )
        }
        return transactionList
    }
}