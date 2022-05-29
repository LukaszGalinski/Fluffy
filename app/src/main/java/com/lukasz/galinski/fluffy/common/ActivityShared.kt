package com.lukasz.galinski.fluffy.common

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast

private const val INACTIVE_BUTTON_ALPHA = .6F
private const val ACTIVE_BUTTON_ALPHA = 1F

fun Context.createToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Button.markAs(state: Boolean) {
    this.isEnabled = state
}

fun Button.setStateAppearance(){
    if (this.isEnabled){this.setActiveStateAppearance()}
    else this.setInactiveStateAppearance()
}

private fun Button.setActiveStateAppearance(){
    this.alpha = ACTIVE_BUTTON_ALPHA
}

private fun Button.setInactiveStateAppearance(){
    this.alpha = INACTIVE_BUTTON_ALPHA
}

fun ProgressBar.setVisible(){
    this.visibility = View.VISIBLE
}

fun ProgressBar.setInvisible(){
    this.visibility = View.GONE
}
