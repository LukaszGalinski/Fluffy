package com.lukasz.galinski.core.domain

sealed class SingleTimeOperationResult {
    object Neutral : SingleTimeOperationResult()
    object Success : SingleTimeOperationResult()
    data class Failure(val message: String) : SingleTimeOperationResult()
}
