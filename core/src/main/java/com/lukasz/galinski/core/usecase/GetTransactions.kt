package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.repository.TransactionsRepository
import kotlinx.coroutines.flow.flowOf

class GetTransactions(private val transactionsRepository: TransactionsRepository) {
    operator fun invoke(userId: Long, startDate: Long, endDate: Long) = flowOf {
        transactionsRepository.getTransactions(userId, startDate, endDate)
    }
}