package com.lukasz.galinski.fluffy.presentation.main

sealed interface MainMenuEvent {
    object ShowFabAnimation: MainMenuEvent
    object HideFabAnimation: MainMenuEvent
}
