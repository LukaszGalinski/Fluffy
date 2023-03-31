package com.lukasz.galinski.fluffy.framework.database.user

import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.core.repository.UsersDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoomUsersDataSource @Inject constructor(private val usersDao: UsersDao) : UsersDataSource {

    override suspend fun addUser(user: User) = usersDao.addNewUser(UserEntity.fromUser(user))

    override fun loginUser(userEmail: String, userPassword: String): Flow<Long> {
        return flow {
            emit(usersDao.loginUser(userEmail, userPassword))
        }
    }

    override suspend fun getUser(userId: Long) = usersDao.getUser(userId).toUser()
}