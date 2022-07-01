package com.lukasz.galinski.fluffy.repository.database.user

import com.lukasz.galinski.fluffy.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUser(userId: Long): Flow<UserModel>
    fun addNewUser(user: UserModel): Flow<Long>
    fun loginUser(userEmail: String, userPassword: String): Flow<Long>
}