package com.lukasz.galinski.fluffy.data.database.transaction

import com.lukasz.galinski.core.usecase.AddTransaction
import com.lukasz.galinski.core.usecase.GetTransactions

data class TransactionUseCases(
    val addTransaction: AddTransaction,
    val getTransactions: GetTransactions,
)
