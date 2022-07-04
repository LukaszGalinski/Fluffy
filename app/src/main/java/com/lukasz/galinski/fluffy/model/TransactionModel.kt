package com.lukasz.galinski.fluffy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

private const val TRANSACTIONS_TABLE = "TransactionsTable"

@Entity(indices = [Index(value = ["transactionId"], unique = true)], tableName = TRANSACTIONS_TABLE)
data class TransactionModel(
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "date")
    var date: Long,
    @ColumnInfo(name = "category")
    var category: String?,
    @ColumnInfo(name = "amount")
    var amount: String?,
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "type")
    var type: String?,
    @ColumnInfo(name = "userId")
    var userId: Long?,
) {
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long? = null
}

