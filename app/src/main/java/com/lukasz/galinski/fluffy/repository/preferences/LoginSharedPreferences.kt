package com.lukasz.galinski.fluffy.repository.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

private const val LOGGED_USER_LABEL = "LoggedUser"

class LoginSharedPreferences @Inject constructor(context: Context) {

    private val appContext = context.applicationContext

    private val sharedPreferences: SharedPreferences
        get() = appContext.getSharedPreferences(LOGGED_USER_LABEL, Context.MODE_PRIVATE)

    fun setLoggedUser(id: Long) {
        sharedPreferences.edit().putLong(
            LOGGED_USER_LABEL,
            id
        ).apply()
    }

    fun getLoggedUser(): Long {
        return sharedPreferences.getLong(LOGGED_USER_LABEL, 0)
    }
}