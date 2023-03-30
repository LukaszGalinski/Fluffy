package com.lukasz.galinski.fluffy.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.galinski.fluffy.data.database.transaction.TransactionEntity
import com.lukasz.galinski.fluffy.data.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.data.database.user.UserEntity
import com.lukasz.galinski.fluffy.data.database.user.UsersDao

private const val DATABASE_VERSION = 2

@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [
        TransactionEntity::class,
        UserEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun transactionsDao(): TransactionsDao
}
