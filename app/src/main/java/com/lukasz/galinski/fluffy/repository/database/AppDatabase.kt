package com.lukasz.galinski.fluffy.repository.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.galinski.fluffy.model.UserModel

private const val DATABASE_VERSION = 1
@Database(
    entities = [UserModel::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): DatabaseDao
}
