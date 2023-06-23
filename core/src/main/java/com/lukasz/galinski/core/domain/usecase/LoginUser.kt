package com.lukasz.galinski.core.domain.usecase

import com.lukasz.galinski.core.domain.BaseResult
import com.lukasz.galinski.core.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class LoginUser(private val usersRepository: UsersRepository) {
    operator fun invoke(userEmail: String, userPassword: String): Flow<BaseResult> {
        return flow {
            usersRepository.loginUser(userEmail, userPassword)
                .onEach {
                    if (it == null) {
                        emit(BaseResult.Error)
                    } else {
                        emit(BaseResult.Success(it))
                    }
                }.collect()
        }
    }
}