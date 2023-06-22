package com.lukasz.galinski.core.domain

sealed class SingleTimeEvent {
    object Neutral : SingleTimeEvent()
    object Success : SingleTimeEvent()
    data class Failure(val message: String) : SingleTimeEvent()
}
