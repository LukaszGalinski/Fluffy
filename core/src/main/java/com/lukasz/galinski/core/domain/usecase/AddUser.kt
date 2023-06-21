package com.lukasz.galinski.core.domain.usecase

import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.core.domain.repository.UsersRepository

class AddUser (private val usersRepository: UsersRepository) {
    operator fun invoke(user: User) = usersRepository.addUser(user)
}