package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.User

class UsersRepository(private val dataSource: UsersDataSource) {
    fun getUser(userId: Long) = dataSource.getUser(userId)
    fun addUser(user: User) = dataSource.addUser(user)
    fun loginUser(userEmail: String, userPassword: String) = dataSource.loginUser(userEmail, userPassword)
}