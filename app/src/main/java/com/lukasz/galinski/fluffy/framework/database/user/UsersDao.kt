package com.lukasz.galinski.fluffy.framework.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsersDao {
    @Insert
    suspend fun addNewUser(user: UserEntity): Long

    @Query("SELECT userId FROM usersTable WHERE userEmail LIKE:email AND password LIKE:password")
    fun loginUser(email: String, password: String): Long

    @Query("SELECT * FROM usersTable")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM usersTable WHERE userId LIKE:userId")
    suspend fun getUser(userId: Long): UserEntity

    @Query("DELETE FROM usersTable WHERE userId LIKE:userId")
    suspend fun deleteUser(userId: Long)
}