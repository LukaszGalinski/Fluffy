package com.lukasz.galinski.fluffy.repository.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val SETTINGS_LABEL = "Settings"
private const val LOGGED_USER_LABEL = "LoggedUser"

private val Context.dataStore by preferencesDataStore(SETTINGS_LABEL)
class PreferencesData @Inject constructor(
    context: Context,
) {
    private val loggedUserPreferences = longPreferencesKey(LOGGED_USER_LABEL)
    private val appContext = context.applicationContext

    private val dataStorePreferences: DataStore<Preferences>
        get() = appContext.dataStore

    suspend fun setLoggedUser(id: Long) {
        dataStorePreferences.edit { settings ->
            settings[loggedUserPreferences] = id
        }
    }

    fun getLoggedUser(): Flow<Long> {
        return dataStorePreferences.data.map { it[loggedUserPreferences] ?: 0 }
    }
}
