package com.lukasz.galinski.fluffy.presentation.account

sealed interface LoginStates
object UserNotFound : LoginStates
data class LoginSuccess(val userId: Long) : LoginStates
