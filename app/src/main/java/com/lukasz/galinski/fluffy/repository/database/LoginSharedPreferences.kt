package com.lukasz.galinski.fluffy.repository.database

import android.content.Context
import android.content.SharedPreferences

private const val LOGGED_USER_LABEL = "LoggedUser"

fun Context.updateLoggedUser(id: Long) {
    val sharedPreferences = this.getSharedPreferences(LOGGED_USER_LABEL, Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    editor.putLong(LOGGED_USER_LABEL, id)
    editor.apply()
}

fun Context.readLoggedUser(): Long {
    val sharedPreferences: SharedPreferences =
        this.getSharedPreferences(
            LOGGED_USER_LABEL, Context.MODE_PRIVATE
        )
    return sharedPreferences.getLong(LOGGED_USER_LABEL, 0)
}