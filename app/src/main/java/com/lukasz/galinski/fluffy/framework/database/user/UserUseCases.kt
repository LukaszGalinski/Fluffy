package com.lukasz.galinski.fluffy.framework.database.user

import com.lukasz.galinski.core.domain.usecase.AddUser
import com.lukasz.galinski.core.domain.usecase.GetUser
import com.lukasz.galinski.core.domain.usecase.LoginUser

data class UserUseCases(
    val addUser: AddUser,
    val getUser: GetUser,
    val loginUser: LoginUser
)
