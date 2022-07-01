package com.lukasz.galinski.fluffy.repository.database.user

import com.lukasz.galinski.fluffy.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(private val db: UsersDao) : UsersRepository {

    override fun addNewUser(user: UserModel) =
        flow {
            val id = db.addNewUser(user)
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