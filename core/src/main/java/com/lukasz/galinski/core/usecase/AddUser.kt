package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.core.repository.UsersRepository
import kotlinx.coroutines.flow.flowOf

class AddUser (private val usersRepository: UsersRepository) {
    operator fun invoke(user: User) = flowOf {
        usersRepository.addUser(user)
    }
}