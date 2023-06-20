package com.lukasz.galinski.fluffy.framework.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lukasz.galinski.core.data.User

private const val USERS_TABLE = "usersTable"

@Entity(indices = [Index(value = ["userEmail"], unique = true)], tableName = USERS_TABLE)
data class UserEntity(
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "userEmail") var userEmail: String?,
    @ColumnInfo(name = "password") var password: String?,
    @ColumnInfo(name = "pin") val pin: String,
    @PrimaryKey (autoGenerate = true) var userId: Long?
) {
    companion object {
        fun fromUser(user: User) = UserEntity(user.name, user.userEmail, user.password, user.pin, userId = null)
    }

    fun toUser() = User(name, userEmail, password, pin)
}

