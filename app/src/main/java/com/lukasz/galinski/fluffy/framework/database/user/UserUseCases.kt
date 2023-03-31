package com.lukasz.galinski.fluffy.framework.database.user

import com.lukasz.galinski.core.usecase.AddUser
import com.lukasz.galinski.core.usecase.GetUser
import com.lukasz.galinski.core.usecase.LoginUser

data class UserUseCases(
    val addUser: AddUser,
    val getUser: GetUser,
    val loginUser: LoginUser
)
