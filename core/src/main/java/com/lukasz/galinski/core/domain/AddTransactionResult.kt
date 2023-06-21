package com.lukasz.galinski.core.domain

sealed class AddTransactionResult<out T> {
    object Success : AddTransactionResult<Nothing>()
    object SuccessInTimeRange: AddTransactionResult<Nothing>()
    data class Error<T: Any>(val message: T): AddTransactionResult<T>()
}
