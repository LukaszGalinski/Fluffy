package com.lukasz.galinski.fluffy.framework.database.transaction

import com.lukasz.galinski.core.domain.usecase.AddTransaction
import com.lukasz.galinski.core.domain.usecase.GetTransactionTotalAmount
import com.lukasz.galinski.core.domain.usecase.GetTransactions

data class TransactionUseCases(
    val addTransaction: AddTransaction,
    val getTransactions: GetTransactions,
    val getTransactionTotalAmount: GetTransactionTotalAmount
)
