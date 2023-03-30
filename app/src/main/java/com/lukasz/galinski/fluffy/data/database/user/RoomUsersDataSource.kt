package com.lukasz.galinski.fluffy.data.database.user

import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.core.repository.UsersDataSource
import javax.inject.Inject

class RoomUsersDataSource @Inject constructor(private val usersDao: UsersDao) : UsersDataSource {

    override fun addUser(user: User) = usersDao.addNewUser(UserEntity.fromUser(user))

    override fun loginUser(userEmail: String, userPassword: String): Long {
        return usersDao.loginUser(userEmail, userPassword)
    }

    override fun getUser(userId: Long): User {
        return usersDao.getUser(userId).toUser()
    }
}