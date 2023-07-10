package com.lukasz.galinski.fluffy.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_DATASTORE_NAME: String = "testDataStore"
private val Context.dataStore by preferencesDataStore(TEST_DATASTORE_NAME)

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DataStoreTest {
    private val loggedUserPreferences = longPreferencesKey(TEST_DATASTORE_NAME)
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val appContext = context.applicationContext
    private lateinit var dataStorePreferences: DataStore<Preferences>

    @Before
    fun createDataStore() {
        dataStorePreferences = appContext.dataStore
    }

    @After
    fun removeDataStore() {
        runBlocking { dataStorePreferences.edit { it.clear() } }
    }

    @Test
    fun readWriteDataStoreData() {
        runTest {
            dataStorePreferences.edit { settings ->
                settings[loggedUserPreferences] = 5
            }
            val storeValue =
                dataStorePreferences.data.map { it[loggedUserPreferences] ?: 0 }.first()
            Assert.assertEquals(5, storeValue)
        }
    }

    @Test
    fun readDefaultDataStoreValue() {
        runTest {
            dataStorePreferences.edit { it.clear() }
            val storeValue =
                dataStorePreferences.data.map { it[loggedUserPreferences] ?: 0 }.first()
            Assert.assertEquals(0, storeValue)
        }
    }
}