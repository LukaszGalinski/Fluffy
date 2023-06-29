package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukasz.galinski.core.domain.BaseResult
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.account.login.LoginUiState
import com.lukasz.galinski.fluffy.presentation.account.login.LoginViewModel
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
    private val userPreferences = mockk<PreferencesData>(relaxed = true)
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
    fun checkLoginSuccessReturnedOnCorrectLoginDetails() = runTest {
        setLoginOutputFromRepository(10)

        coEvery { mockedUserCases.addUser(any()) }.returns(flowOf(5))

        val statesList = mutableListOf<LoginUiState>()

        val job = loginViewModel.loginUiEvent
            .onEach { statesList.add(it) }
            .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

        loginViewModel.loginUser(userNameLogin, userNamePassword)
        advanceUntilIdle()

        assertEquals(statesList[0], LoginUiState.Idle)
        assertEquals(statesList[1], LoginUiState.IsLoading(true))
        assertEquals(statesList[2], LoginUiState.IsLoading(false))
        assertEquals(statesList[3], LoginUiState.LoginSuccess)
        assertEquals(statesList[4], LoginUiState.Idle)

        job.cancel()
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
                emit(BaseResult.Success(value))
            })
    }
}
