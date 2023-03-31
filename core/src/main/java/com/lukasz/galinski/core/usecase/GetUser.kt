package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.repository.UsersRepository

class GetUser (private val usersRepository: UsersRepository) {
    suspend operator fun invoke(userId: Long) = usersRepository.getUser(userId)
}