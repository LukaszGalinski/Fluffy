package com.lukasz.galinski.fluffy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lukasz.galinski.fluffy.framework.database.AppDatabase
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionsDao
import com.lukasz.galinski.fluffy.framework.database.user.UsersDao
import com.lukasz.galinski.fluffy.viewmodel.DateTools
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseEntityTest {
    private lateinit var usersDao: UsersDao
    private lateinit var transactionsDao: TransactionsDao
    private lateinit var appDatabase: AppDatabase
    private val dummyUserLogin = "test@test.com"
    private val dummyUserPassword = "test"

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lukasz.galinski.fluffy", appContext.packageName)
    }

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        usersDao = appDatabase.usersDao()
        transactionsDao = appDatabase.transactionsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun insertNewUser() {
        val testUser = TestUtilities.createTestUsers(0).apply {
            get(0).name = "Clint"
        }
        usersDao.addNewUser(testUser[0])
        val allUsers = usersDao.getAllUsers()
        assertEquals(true, allUsers.contains(testUser[0]))
    }

    @Test
    @Throws(Exception::class)
    fun entityWriteAndReadDatabaseElementTest() {
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
    fun entityDeleteTest() {
        val testUsers = TestUtilities.createTestUsers(0)
        usersDao.addNewUser(testUsers[0])
        usersDao.deleteUser(1)
        val allUsers = usersDao.getAllUsers()
        assertEquals(false, allUsers.contains(testUsers[0]))
    }

    @Test
    @Throws(Exception::class)
    fun loadSingleUser() {
        val testUsers = TestUtilities.createTestUsers(1)
        val newUser = usersDao.addNewUser(testUsers[0])
        val loadedUser = usersDao.getUser(newUser)
        assertEquals(testUsers[0], loadedUser)
    }

    @Test
    fun loginUserWithSuccess() {
        val testUser = TestUtilities.createTestUsers(0).apply {
            get(0).userEmail = dummyUserLogin
            get(0).password = dummyUserPassword
        }
        usersDao.addNewUser(testUser[0])

        val userLoginSuccessValue = usersDao.loginUser(dummyUserLogin, dummyUserPassword)
        assertEquals(1, userLoginSuccessValue)
    }

    @Test
    fun loginUserWithFailure() {
        val userLoggedOutValue = usersDao.loginUser(dummyUserLogin, dummyUserPassword)
        assertEquals(0, userLoggedOutValue)
    }

    @Test
    fun loadTransactionsInDateRange(){
        val date = DateTools()
        val currentDate = date.getCurrentDateInLong()
        val startMonthDate = date.getStartMonthDate()
        val endMonthDate = date.getEndMonthDate()
        val dummyTransaction = TestUtilities.createTestTransactions(2).apply {
            get(0).date = currentDate
            get(1).date = 9999
            get(2).date = currentDate+4000
        }
        transactionsDao.addNewTransaction(dummyTransaction[0])
        transactionsDao.addNewTransaction(dummyTransaction[1])
        transactionsDao.addNewTransaction(dummyTransaction[2])
        println(dummyTransaction)

        val transactionsCount = transactionsDao.getMonthTransactions(1, startMonthDate, endMonthDate).count()
        assertEquals(2, transactionsCount)
    }
}
