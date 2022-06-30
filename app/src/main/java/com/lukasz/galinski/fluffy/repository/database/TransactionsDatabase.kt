package com.lukasz.galinski.fluffy.repository.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.galinski.fluffy.model.DataModel

private const val DATABASE_VERSION = 1
@Database(
    entities = [DataModel::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionsDao(): TransactionsDao
}
