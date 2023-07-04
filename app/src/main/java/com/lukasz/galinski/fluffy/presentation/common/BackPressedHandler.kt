package com.lukasz.galinski.fluffy.presentation.common

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.handleBackPress(backPressedAction: () -> Unit) {
    val backPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressedAction.invoke()
            println("INVOKED")
        }
    }

    requireActivity().onBackPressedDispatcher.addCallback(backPress)
}