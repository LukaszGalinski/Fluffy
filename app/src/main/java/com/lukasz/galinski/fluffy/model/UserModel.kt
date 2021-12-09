package com.lukasz.galinski.fluffy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["userEmail"], unique = true)])
data class UserModel(
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "userEmail") var userEmail: String?,
    @ColumnInfo(name = "password") var password: String?,
    @ColumnInfo(name = "pin") var pin: String,
) {
    @PrimaryKey(autoGenerate = true)
    var userId: Long? = null
}

