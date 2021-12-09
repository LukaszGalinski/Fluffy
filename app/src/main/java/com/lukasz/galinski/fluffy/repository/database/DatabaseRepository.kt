package com.lukasz.galinski.fluffy.repository.database

import com.lukasz.galinski.fluffy.model.UserModel

class DatabaseRepository(private val db: DatabaseDao) {

    fun addNewUser(user: UserModel): Long {
        return db.addNewUser(user)
    }

    /*fun loginUser(email: String, password: String): Long {
        return db.loginUser(email, password)
    }

    fun checkEmailUnique(email: String): Long {
        return db.checkEmailUnique(email)
    }

    fun getUser(userID: Long): UserModel {
        return db.getUser(userID)
    }*/
}