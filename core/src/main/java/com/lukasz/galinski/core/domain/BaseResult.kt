package com.lukasz.galinski.core.domain

sealed class BaseResult {
    data class Success(val userId: Long) : BaseResult()
    object Error : BaseResult()
}
