package com.lukasz.galinski.fluffy.presentation.account

sealed class AccountStates : LoginStates, RegisterStates
object Idle : AccountStates()
object Loading : AccountStates()
data class Failure(val exception: Throwable) : AccountStates()

