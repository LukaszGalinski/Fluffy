package com.lukasz.galinski.fluffy.framework.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionEntity
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.framework.database.user.UserEntity
import com.lukasz.galinski.fluffy.framework.database.user.UsersDao

private const val DATABASE_VERSION = 3

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
