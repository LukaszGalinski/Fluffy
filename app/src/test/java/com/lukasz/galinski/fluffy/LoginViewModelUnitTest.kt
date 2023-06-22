package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.account.Idle
import com.lukasz.galinski.fluffy.presentation.account.Loading
import com.lukasz.galinski.fluffy.presentation.account.RegisterStates
import com.lukasz.galinski.fluffy.presentation.account.RegisterSuccess
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockedUserCases = mockk<UserUseCases>()
    private val mockedUser = mockk<User>()
    private val userPreferences = mockk<PreferencesData>()
    private lateinit var loginViewModel: LoginViewModel
    private val userNameLogin = "admin"
    private val userNamePassword = "admin"

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coJustRun { userPreferences.setLoggedUser(any()) }
        loginViewModel = spyk(LoginViewModel(mockedUserCases, userPreferences, testDispatcher))
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
    }

    @Test
    fun checkLoginSuccessReturnedOnCorrectLoginDetails() {
        setLoginOutputFromRepository(10)

        runTest {
            loginViewModel.loginUser(userNameLogin, userNamePassword)
            advanceUntilIdle()
        }

        val loggedUser =
            getPrivateFieldValue(loginViewModel, "currentlyLoggedUserId", Long::class.java)
        assertEquals(10L, loggedUser)
    }

    @Test
    fun checkLoginFailedReturnedOnUserNotFound() {
        setLoginOutputFromRepository(0)
        every { userPreferences.getLoggedUser() }.returns(flowOf(0))

        runTest {
            loginViewModel.loginUser(userNameLogin, userNamePassword)
            assertEquals(0L, loginViewModel.getLoggedUser().first())
        }
    }

    @Test
    fun checkButtonStateChange() {
        runTest {
            loginViewModel.setSaveButtonState(true)
            assertEquals(true, loginViewModel.saveButtonState.value)

            loginViewModel.setSaveButtonState(false)
            assertEquals(false, loginViewModel.saveButtonState.value)
        }
    }

    @Test
    fun checkRegisterSuccessFlowReturnedOnUserAdded() = runTest {
        coEvery { mockedUserCases.addUser(any()) }.returns(flowOf(5))

        val statesList = mutableListOf<RegisterStates>()

        val job = loginViewModel.userRegisterStates
            .onEach { statesList.add(it) }
            .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

        loginViewModel.saveUserIntoDatabase(mockedUser)
        advanceUntilIdle()

        assertEquals(statesList[0], Idle)
        assertEquals(statesList[1], Loading)
        assertEquals(statesList[2], RegisterSuccess(5))
        assertEquals(statesList[3], Idle)

        job.cancel()
    }



    @Test
    fun checkLoggedUserIdReturned() {
        every { userPreferences.getLoggedUser() }.returns(flowOf(5))

        runTest {
            val returnedValue = loginViewModel.getLoggedUser().first()!!
            assertEquals(5, returnedValue)
        }
    }

    private fun setLoginOutputFromRepository(value: Long) {
        every { mockedUserCases.loginUser(any(), any()) }
            .returns(flow {
                emit(value)
            })
    }
}
