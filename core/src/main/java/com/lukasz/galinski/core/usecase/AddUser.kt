package com.lukasz.galinski.core.usecase

import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.core.repository.UsersRepository

class AddUser (private val usersRepository: UsersRepository) {
    suspend operator fun invoke(user: User) = usersRepository.addUser(user)
}