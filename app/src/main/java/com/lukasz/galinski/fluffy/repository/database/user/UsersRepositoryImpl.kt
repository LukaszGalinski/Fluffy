package com.lukasz.galinski.fluffy.repository.database.user

import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.UsersDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(private val usersDao: UsersDao) : UsersRepository {

    override fun addNewUser(user: UserModel) =
        flow {
            val id = usersDao.addNewUser(user)
            emit(id)
        }

    override fun loginUser(userEmail: String, userPassword: String): Flow<Long> {
        return flow {
            val userId = usersDao.loginUser(userEmail, userPassword)
            emit(userId)
        }
    }

    override fun getUser(userId: Long): Flow<UserModel> {
        return flow {
            val user = usersDao.getUser(userId)
            emit(user)
        }
    }
}