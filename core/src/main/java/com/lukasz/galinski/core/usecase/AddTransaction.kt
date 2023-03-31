package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.repository.TransactionsRepository

class AddTransaction (private val transactionsRepository: TransactionsRepository) {
    suspend operator fun invoke(transaction: Transaction) =
        transactionsRepository.addTransaction(transaction)
}