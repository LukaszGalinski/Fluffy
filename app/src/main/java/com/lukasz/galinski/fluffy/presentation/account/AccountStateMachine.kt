package com.lukasz.galinski.fluffy.presentation.account

sealed class AccountStates
object Idle : AccountStates()
object Loading : AccountStates()
object UserNotFound : AccountStates()
data class Success(val userId: Long) : AccountStates()
data class Failure(val exception: Throwable) : AccountStates()