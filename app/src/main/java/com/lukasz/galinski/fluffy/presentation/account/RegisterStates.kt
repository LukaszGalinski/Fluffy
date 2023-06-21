package com.lukasz.galinski.fluffy.presentation.account

sealed interface RegisterStates
data class RegisterSuccess(val userId: Long): RegisterStates
