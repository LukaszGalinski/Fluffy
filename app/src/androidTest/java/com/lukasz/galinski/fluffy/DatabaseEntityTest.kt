package com.lukasz.galinski.fluffy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lukasz.galinski.fluffy.repository.database.AppDatabase
import com.lukasz.galinski.fluffy.repository.database.DatabaseDao
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.lang.Exception
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class DatabaseEntityTest {
    private lateinit var usersDao: DatabaseDao
    private lateinit var usersDatabase: AppDatabase

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lukasz.galinski.fluffy", appContext.packageName)
    }

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        usersDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        usersDao = usersDatabase.usersDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        usersDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun entityWriteAndReadDatabaseElementTest(){
        val testUsers = TestUtilities.createTestUsers(1).apply {
            get(0).name = "Mark"
            get(1).name = "John"
        }
        usersDao.addNewUser(testUsers[0])
        usersDao.addNewUser(testUsers[1])

        val readUserFirst = usersDao.getUser(1)
        val readUserSecond = usersDao.getUser(2)
        assertEquals(testUsers, arrayListOf(readUserFirst, readUserSecond))
    }

    @Test
    @Throws(Exception::class)
    fun entityDeleteTest(){
        val testUsers = TestUtilities.createTestUsers(1)
        usersDao.addNewUser(testUsers[0])
        usersDao.addNewUser(testUsers[1])
        usersDao.deleteUser(1)

        val list = usersDao.getAllUsers()
        assertEquals(1, list.size)
    }
}