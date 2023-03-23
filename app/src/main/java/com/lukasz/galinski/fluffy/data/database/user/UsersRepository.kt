package com.lukasz.galinski.fluffy.data.database.user

import com.lukasz.galinski.fluffy.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getUser(userId: Long): Flow<UserModel>
    fun addNewUser(user: UserModel): Flow<Long>
    fun loginUser(userEmail: String, userPassword: String): Flow<Long>
}