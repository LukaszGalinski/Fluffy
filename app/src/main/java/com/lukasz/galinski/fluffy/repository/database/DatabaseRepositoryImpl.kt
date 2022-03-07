package com.lukasz.galinski.fluffy.repository.database

import com.lukasz.galinski.fluffy.model.UserModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(private val db: DatabaseDao) : DatabaseRepository {

    override fun addNewUser(user: UserModel) =
        flow {
            val id = db.addNewUser(user)
            delay(5000)
            emit(id)
        }

    override fun loginUser(userEmail: String, userPassword: String): Flow<Long> {
        return flow {
            val userId = db.loginUser(userEmail, userPassword)
            emit(userId)
        }
    }

    override fun getUser(userId: Long): Flow<UserModel> {
        return flow {
            val user = db.getUser(userId)
            emit(user)
        }
    }
}