package com.lukasz.galinski.fluffy.repository.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.model.UserModel

private const val DATABASE_VERSION = 2

@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [
        TransactionModel::class,
        UserModel::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}
