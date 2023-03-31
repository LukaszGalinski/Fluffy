package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.User
import kotlinx.coroutines.flow.Flow

interface UsersDataSource {
    suspend fun getUser(userId: Long): User
    suspend fun addUser(user: User): Long
    fun loginUser(userEmail: String, userPassword: String): Flow<Long>
}