package com.lukasz.galinski.fluffy.repository.database

import com.lukasz.galinski.fluffy.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getUser(userId: Long): Flow<UserModel>
    fun addNewUser(user: UserModel): Flow<Long>
    fun loginUser(userEmail: String, userPassword: String): Flow<Long>
}