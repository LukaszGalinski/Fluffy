package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukasz.galinski.core.data.Transaction
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionUseCases
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.viewmodel.DateTools
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainMenuViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private val transactionRepository = mockk<TransactionUseCases>()
    private val userRepository = mockk<UserUseCases>()
    private val mockedUser = mockk<User>()
    private val userPreferences = mockk<PreferencesData>()
    private val mockedTransaction = mockk<Transaction>(relaxed = true)
    private val mockedDateTools = mockk<DateTools>()
    private lateinit var mainMenuViewModel: MainMenuViewModel

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        Dispatchers.setMain(testDispatcher)
        mainMenuViewModel = spyk(
            MainMenuViewModel(
                userRepository,
                transactionRepository,
                userPreferences,
                testDispatcher
            ),
            recordPrivateCalls = true
        )

        replacePrivateField(mainMenuViewModel, "dateTools", mockedDateTools)
        val transactionList = arrayListOf(mockedTransaction, mockedTransaction)

        coEvery { transactionRepository.getTransactions(any(), any(), any()) }.returns(
            flowOf(transactionList)
        )
        coEvery { transactionRepository.addTransaction(any()) }.returns(flowOf(1))
        coEvery { userRepository.getUser(any()) }.returns(flowOf(mockedUser))
        coEvery { userPreferences.getLoggedUser() }.returns(flowOf(5))
    }

    @After
    fun clean() {
        Dispatchers.resetMain()
        RxAndroidPlugins.reset()
    }

    @Test
    fun logoutCurrentUser() = runTest {
        mainMenuViewModel.logoutUser()
        advanceUntilIdle()

        coVerify(exactly = 1) { userPreferences.setLoggedUser(0) }
        assertEquals(0, mainMenuViewModel.userID.value)
    }

    @Test
    fun checkUserDataLoadedWithCorrectUserID() = runTest {
        advanceUntilIdle()

        coVerify(exactly = 1) { userPreferences.getLoggedUser() }
        assertEquals(5, mainMenuViewModel.userID.value)
    }

    @Test
    fun checkUserTransactionsLoadedAndSet() = runTest {
        advanceUntilIdle()

        coVerify { transactionRepository.getTransactions(5, any(), any()) }
        assertEquals(2, mainMenuViewModel.transactionList.value.size)
    }

    @Test
    fun checkTransactionListNotUpdatedWhenDateNotInRange() = runTest {
        every { mockedTransaction.date } returns 20

        mainMenuViewModel.addNewTransaction(mockedTransaction)
        advanceUntilIdle()

        verify(exactly = 1) { transactionRepository.addTransaction(any()) }
        assertEquals(2, mainMenuViewModel.transactionList.value.size)
    }

    @Test
    fun checkTransactionListUpdatedWhenDateInRange() = runTest {
        every { mockedTransaction.date } returns 0

        mainMenuViewModel.addNewTransaction(mockedTransaction)
        advanceUntilIdle()

        verify(exactly = 1) { transactionRepository.addTransaction(any()) }
        assertEquals(2, mainMenuViewModel.transactionList.value.size)
    }

    @Test
    fun checkCurrentMonthNumberLoaded() = runTest {
        every { mockedDateTools.getCurrentMonthNumber() } returns 3
        val loadedMonth = mainMenuViewModel.getCurrentMonth()
        assertEquals(3, loadedMonth)
    }

    @Test
    fun checkCurrentDateValueLoaded() = runTest {
        every { mockedDateTools.getCurrentDateInLong() } returns 987
        assertEquals(987, mainMenuViewModel.getCurrentDate())
    }
}