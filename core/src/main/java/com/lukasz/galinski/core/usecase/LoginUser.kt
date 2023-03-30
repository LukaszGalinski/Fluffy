package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.repository.UsersRepository
import kotlinx.coroutines.flow.flowOf

class LoginUser (private val usersRepository: UsersRepository) {
    operator fun invoke(userEmail: String, userPassword: String) = flowOf {
        usersRepository.loginUser(userEmail, userPassword)
    }
}