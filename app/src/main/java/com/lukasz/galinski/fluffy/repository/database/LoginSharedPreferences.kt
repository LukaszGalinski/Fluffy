package com.lukasz.galinski.fluffy.repository.database

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

private const val LOGGED_USER_LABEL = "LoggedUser"

class LoginSharedPreferences @Inject constructor(context: Context) {

    private val appContext = context.applicationContext

    private val sharedPreferences: SharedPreferences
        get() = appContext.getSharedPreferences(LOGGED_USER_LABEL, Context.MODE_PRIVATE)

    fun updateLoggedUser(id: Long) {
        sharedPreferences.edit().putLong(
            LOGGED_USER_LABEL,
            id
        ).apply()
    }

    fun readLoggedUser(): Long {
        return sharedPreferences.getLong(LOGGED_USER_LABEL, 0)
    }
}