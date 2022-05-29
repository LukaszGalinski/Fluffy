package com.lukasz.galinski.fluffy.view.main

sealed class MainMenuStates
object Idle : MainMenuStates()
object Loading : MainMenuStates()
object Failure : MainMenuStates()
