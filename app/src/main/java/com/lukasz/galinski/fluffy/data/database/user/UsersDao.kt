package com.lukasz.galinski.fluffy.data.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lukasz.galinski.fluffy.data.model.UserModel

@Dao
interface UsersDao {

    @Insert
    fun addNewUser(user: UserModel): Long

    @Query("SELECT userId FROM usersTable WHERE userEmail LIKE:email AND password LIKE:password")
    fun loginUser(email: String, password: String): Long

    @Query("SELECT * FROM usersTable")
    fun getAllUsers(): List<UserModel>

    @Query("SELECT * FROM usersTable WHERE userId LIKE:userId")
    fun getUser(userId: Long): UserModel

    @Query("DELETE FROM usersTable WHERE userId LIKE:userId")
    fun deleteUser(userId: Long)
}