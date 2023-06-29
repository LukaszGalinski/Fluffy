package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.presentation.account.register.RegisterUiEvent
import com.lukasz.galinski.fluffy.presentation.account.register.RegisterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class RegisterViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private val mockedUserCases = mockk<UserUseCases>()
    private val mockedUser = mockk<User>()
    private lateinit var registerViewModel: RegisterViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        registerViewModel = spyk(RegisterViewModel(mockedUserCases, testDispatcher))
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
    }

    @Test
    fun checkButtonStateChange() {
        runTest {
            registerViewModel.setSaveButtonState(true)
            assertEquals(true, registerViewModel.saveButtonState.value)

            registerViewModel.setSaveButtonState(false)
            assertEquals(false, registerViewModel.saveButtonState.value)
        }
    }

    @Test
    fun checkRegisterSuccessFlowReturnedOnUserAdded() = runTest {
        coEvery { mockedUserCases.addUser(any()) }.returns(flowOf(5))

        val statesList = mutableListOf<RegisterUiEvent>()

        val job = registerViewModel.registerUiEvent
            .onEach { statesList.add(it) }
            .launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))

        registerViewModel.saveUserIntoDatabase(mockedUser)
        advanceUntilIdle()

        assertEquals(statesList[0], RegisterUiEvent.Idle)
        assertEquals(statesList[1], RegisterUiEvent.IsLoading(true))
        assertEquals(statesList[2], RegisterUiEvent.IsLoading(false))
        assertEquals(statesList[3], RegisterUiEvent.RegisterSuccess(5))
        assertEquals(statesList[4], RegisterUiEvent.Idle)

        job.cancel()
    }
}
