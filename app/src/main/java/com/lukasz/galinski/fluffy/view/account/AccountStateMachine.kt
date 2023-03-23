package com.lukasz.galinski.fluffy.view.account

sealed class AccountStates
object Idle : AccountStates()
object Loading : AccountStates()
data class Success(val userId: Long) : AccountStates()
data class UserNotFound(val errorValue: Long) : AccountStates()
data class Failure(val exception: Throwable) : AccountStates()
