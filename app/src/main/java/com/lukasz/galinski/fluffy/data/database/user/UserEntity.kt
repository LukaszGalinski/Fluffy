package com.lukasz.galinski.fluffy.data.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lukasz.galinski.core.data.User

private const val USERS_TABLE = "usersTable"

@Entity(indices = [Index(value = ["userEmail"], unique = true)], tableName = USERS_TABLE)
data class UserEntity(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "userEmail") val userEmail: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "pin") val pin: String,
    @PrimaryKey(autoGenerate = true) val userId: Long = 0L
) {
    companion object {
        fun fromUser(user: User) = UserEntity(user.name, user.userEmail, user.password, user.pin)
    }

    fun toUser() = User(name, userEmail, password, pin, userId)
}

