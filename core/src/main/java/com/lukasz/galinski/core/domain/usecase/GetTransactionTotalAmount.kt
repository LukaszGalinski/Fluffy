package com.lukasz.galinski.core.domain.usecase

import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.domain.TransactionType
import kotlin.math.round


class GetTransactionTotalAmount {
    operator fun invoke(transactionType: TransactionType, transactions: List<Transaction>): Double =
        round(
            transactions.filter { item ->
                item.type == transactionType.label
            }.sumOf {
                it.amount!!
            }
        )
}