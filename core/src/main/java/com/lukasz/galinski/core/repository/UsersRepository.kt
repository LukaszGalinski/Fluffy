package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.User
import kotlinx.coroutines.flow.Flow

class UsersRepository(private val dataSource: UsersDataSource) {
    suspend fun getUser(userId: Long) = dataSource.getUser(userId)
    suspend fun addUser(user: User) = dataSource.addUser(user)
    fun loginUser(userEmail: String, userPassword: String): Flow<Long> {
        return dataSource.loginUser(userEmail, userPassword)
    }
}