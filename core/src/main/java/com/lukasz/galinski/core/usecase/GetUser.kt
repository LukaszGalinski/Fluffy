package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.repository.UsersRepository
import kotlinx.coroutines.flow.flowOf

class GetUser (private val usersRepository: UsersRepository) {
    operator fun invoke(userId: Long) = flowOf {
        usersRepository.getUser(userId)
    }
}