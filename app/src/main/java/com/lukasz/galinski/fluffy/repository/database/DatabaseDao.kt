package com.lukasz.galinski.fluffy.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lukasz.galinski.fluffy.model.UserModel

@Dao
interface DatabaseDao {

    @Insert
    fun addNewUser(user: UserModel): Long

    @Query("SELECT userId FROM UserModel WHERE userEmail LIKE:email AND password LIKE:password")
    fun loginUser(email: String, password: String): Long

    @Query("SELECT * FROM UserModel")
    fun getAllUsers(): List<UserModel>

    @Query("SELECT * FROM UserModel WHERE userId LIKE:userId")
    fun getUser(userId: Long): UserModel
}