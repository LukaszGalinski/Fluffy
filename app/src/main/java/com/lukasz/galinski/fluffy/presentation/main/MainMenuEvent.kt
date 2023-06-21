package com.lukasz.galinski.fluffy.presentation.main

sealed class MainMenuEvent {
    object Idle: MainMenuEvent()
    object ShowFabAnimation: MainMenuEvent()
    object HideFabAnimation: MainMenuEvent()
    data class DisplayToast(val message: String): MainMenuEvent()
    data class IsLoading(val isLoading: Boolean) : MainMenuEvent()
}
