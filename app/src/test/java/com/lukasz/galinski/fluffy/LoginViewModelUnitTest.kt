package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukasz.galinski.fluffy.data.model.UserModel
import com.lukasz.galinski.fluffy.data.database.user.UsersRepositoryImpl
import com.lukasz.galinski.fluffy.data.preferences.PreferencesData
import com.lukasz.galinski.fluffy.view.account.Success
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel
import io.mockk.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
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
    private val userRepository = mockk<UsersRepositoryImpl>()
    private val mockedUser = mockk<UserModel>()
    private val userPreferences = mockk<PreferencesData>()
    private lateinit var loginViewModel: LoginViewModel
    private val userNameLogin = "admin"
    private val userNamePassword = "admin"

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        Dispatchers.setMain(testDispatcher)

        coJustRun { userPreferences.setLoggedUser(any()) }
        coEvery { userRepository.addNewUser(mockedUser) }.returns(flow { emit(5) })
        loginViewModel = spyk(LoginViewModel(userRepository, userPreferences, testDispatcher))
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
        RxAndroidPlugins.reset()
    }

    @Test
    fun checkLoginSuccessReturnedOnCorrectLoginDetails() {
        setLoginOutputFromRepository(10)
        runTest {
            loginViewModel.loginUser(userNameLogin, userNamePassword)
        }
        runTest {
            assertEquals(10, loginViewModel.getLoggedUser().first())
        }
    }

    @Test
    fun checkLoginFailedReturnedOnUserNotFound() {
        setLoginOutputFromRepository(0)
        every { userPreferences.getLoggedUser() }.returns(flowOf(0))
        runTest {
            loginViewModel.loginUser(userNameLogin, userNamePassword)
        }
        runTest {
            assertEquals(0, loginViewModel.getLoggedUser().first())
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
    fun checkRegisterSuccessReturnedOnUserAdded() {
        runTest {
            loginViewModel.saveUserIntoDatabase(mockedUser)
        }
        assertEquals(Success(5), loginViewModel.userAccountState.value)
    }

    @Test
    fun checkLoggedUserIdReturned() {
        every { userPreferences.getLoggedUser() }.returns(flowOf(5))
        var loggedUser = 0L
        runTest {
            loggedUser = loginViewModel.getLoggedUser().first()
        }
        assertEquals(5, loggedUser)
    }

    private fun setLoginOutputFromRepository(value: Long) {
        every { userRepository.loginUser(any(), any()) }
            .returns(flow {
                emit(value)
            })
    }
}
