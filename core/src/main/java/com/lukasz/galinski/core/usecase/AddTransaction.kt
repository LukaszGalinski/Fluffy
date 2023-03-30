package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.repository.TransactionsRepository
import kotlinx.coroutines.flow.flowOf

class AddTransaction (private val transactionsRepository: TransactionsRepository) {
    operator fun invoke(transaction: Transaction) = flowOf{
        transactionsRepository.addTransaction(transaction)
    }
}