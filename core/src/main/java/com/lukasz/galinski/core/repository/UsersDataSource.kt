package com.lukasz.galinski.core.repository

import com.lukasz.galinski.core.data.User

interface UsersDataSource {
    fun getUser(userId: Long): User
    fun addUser(user: User): Long
    fun loginUser(userEmail: String, userPassword: String): Long
}