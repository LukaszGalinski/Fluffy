package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.repository.TransactionsRepository

class GetTransactions(private val transactionsRepository: TransactionsRepository) {
    operator fun invoke(userId: Long, startDate: Long, endDate: Long) =
        transactionsRepository.getTransactions(userId, startDate, endDate)
}