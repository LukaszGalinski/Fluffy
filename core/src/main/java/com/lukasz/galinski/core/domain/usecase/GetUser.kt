package com.lukasz.galinski.core.domain.usecase

import com.lukasz.galinski.core.domain.repository.UsersRepository

class GetUser(private val usersRepository: UsersRepository) {
    operator fun invoke(userId: Long) = usersRepository.getUser(userId)
}