package com.lukasz.galinski.fluffy.repository.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lukasz.galinski.fluffy.HiltApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val LOGGED_USER_LABEL = "LoggedUser"
private const val SETTINGS_LABEL = "Settings"
private val LOGGED_USER_SETTINGS = longPreferencesKey(LOGGED_USER_LABEL)

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_LABEL)

class LoginSharedPreferences @Inject constructor(
    context: Context,
    @HiltApplication.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private val appContext = context.applicationContext

    private val sharedPreferences: DataStore<Preferences>
        get() = appContext.dataStore

    suspend fun setLoggedUser(id: Long) {
        sharedPreferences.edit { settings ->
            settings[LOGGED_USER_SETTINGS] = id
        }
    }

    suspend fun getLoggedUser(): Long {
        return appContext.dataStore.data
            .map { preferences ->
                preferences[LOGGED_USER_SETTINGS] ?: 0
            }.flowOn(ioDispatcher).first()
    }
}