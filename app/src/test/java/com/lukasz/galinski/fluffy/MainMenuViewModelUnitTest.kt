package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.transaction.TransactionsRepositoryImpl
import com.lukasz.galinski.fluffy.repository.database.user.UsersRepositoryImpl
import com.lukasz.galinski.fluffy.repository.preferences.PreferencesData
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import io.mockk.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
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
    private val transactionRepository = mockk<TransactionsRepositoryImpl>()
    private val userRepository = mockk<UsersRepositoryImpl>()
    private val mockedUser = mockk<UserModel>()
    private val userPreferences = mockk<PreferencesData>(relaxed = true)
    private lateinit var mainMenuViewModel: MainMenuViewModel
    private val mockedTransaction = mockk<TransactionModel>(relaxed = true)

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
            ), recordPrivateCalls = true
        )
        val transactionList = ArrayList<TransactionModel>()
        transactionList.add(mockedTransaction)
        transactionList.add(mockedTransaction)

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
    fun logoutCurrentUser() {
        runTest {
            mainMenuViewModel.logoutUser()
        }
        coVerify(exactly = 1) { userPreferences.setLoggedUser(0) }
        assertEquals(0, mainMenuViewModel.userID.value)
    }

    @Test
    fun checkUserDataLoadedWithCorrectUserID() {
        runTest {}
        coVerify(exactly = 1, timeout = 1000) { userPreferences.getLoggedUser() }
        assertEquals(5, mainMenuViewModel.userID.value)
    }

    @Test
    fun checkUserTransactionsLoadedAndSet() {
        runTest {}
        coVerify { transactionRepository.getTransactions(5, any(), any()) }
        assertEquals(2, mainMenuViewModel.transactionList.value.size)
    }

    @Test
    fun checkTransactionListNotUpdatedWhenDateNotInRange() {
        every { mockedTransaction.date } returns 20
        runTest {
            mainMenuViewModel.addNewTransaction(mockedTransaction)
        }
        verify(timeout = 1000, exactly = 1) { transactionRepository.addTransaction(any()) }
        assertEquals(2, mainMenuViewModel.transactionList.value.size)
    }

    @Test
    fun checkTransactionListUpdatedWhenDateInRange() {
        runTest {}
        every { mockedTransaction.date } returns 0
        runTest {
            mainMenuViewModel.addNewTransaction(mockedTransaction)
        }
        verify(timeout = 1000, exactly = 1) { transactionRepository.addTransaction(any()) }
        assertEquals(3, mainMenuViewModel.transactionList.value.size)
    }
}