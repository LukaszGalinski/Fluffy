package com.lukasz.galinski.core.domain.usecase

import com.lukasz.galinski.core.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow

class LoginUser(private val usersRepository: UsersRepository) {
    operator fun invoke(userEmail: String, userPassword: String): Flow<Long?> {
        return usersRepository.loginUser(userEmail, userPassword)
    }
}