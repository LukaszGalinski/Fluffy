package com.lukasz.galinski.fluffy.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.galinski.fluffy.data.model.TransactionModel
import com.lukasz.galinski.fluffy.data.model.UserModel
import com.lukasz.galinski.fluffy.data.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.data.database.user.UsersDao

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
    abstract fun usersDao(): UsersDao
    abstract fun transactionsDao(): TransactionsDao
}
