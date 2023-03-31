package com.lukasz.galinski.fluffy.framework.database.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lukasz.galinski.core.data.Transaction

private const val TRANSACTIONS_TABLE = "TransactionsTable"

@Entity(indices = [Index(value = ["transactionId"], unique = true)], tableName = TRANSACTIONS_TABLE)
data class TransactionEntity(
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "category")
    val category: String?,
    @ColumnInfo(name = "amount")
    val amount: Double?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "type")
    val type: String?,
    @ColumnInfo(name = "userId")
    val userId: Long,
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long = 0L
) {
    companion object {
        fun fromTransaction(transaction: Transaction) = TransactionEntity(
            transaction.name,
            transaction.date,
            transaction.category,
            transaction.amount,
            transaction.description,
            transaction.type,
            transaction.userId,
            transaction.transactionId
        )
    }

    fun toTransaction() = Transaction(name, date, category, amount, description, type, userId, transactionId)
}

