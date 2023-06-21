package com.lukasz.galinski.core.domain.repository

import com.lukasz.galinski.core.data.User
import kotlinx.coroutines.flow.Flow

interface UsersDataSource {
    fun getUser(userId: Long): Flow<User>
    fun addUser(user: User): Flow<Long>
    fun loginUser(userEmail: String, userPassword: String): Flow<Long?>
}