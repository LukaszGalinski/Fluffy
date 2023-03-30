package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lukasz.galinski.fluffy.data.database.transaction.RoomTransactionsDataSource
import com.lukasz.galinski.fluffy.data.database.user.RoomUsersDataSource
import com.lukasz.galinski.fluffy.data.model.TransactionEntity
import com.lukasz.galinski.fluffy.data.model.UserEntity
import com.lukasz.galinski.fluffy.data.preferences.PreferencesData
import com.lukasz.galinski.fluffy.view.main.MainScreen
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MainMenuScreenUITest {

    private val testDispatcher = StandardTestDispatcher()
    private val transactionRepository = mockk<RoomTransactionsDataSource>()
    private val userRepository = mockk<RoomUsersDataSource>()
    private val mockedUser = mockk<UserEntity>()
    private val userPreferences = mockk<PreferencesData>(relaxed = true)
    private lateinit var mainMenuViewModel: MainMenuViewModel
    private val mockedTransaction = mockk<TransactionEntity>(relaxed = true)

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun prepareView() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        hiltRule.inject()

        mainMenuViewModel = spyk(
            MainMenuViewModel(
                userRepository,
                transactionRepository,
                userPreferences,
                testDispatcher
            ), recordPrivateCalls = true
        )
    }

    @Test
    @Ignore("Will be turned on when reflection function will be added")
    fun checkIncomeAndOutcomePriceDisplayed() {
        every { mainMenuViewModel.transactionIncome.value } returns 12.0
        every { mainMenuViewModel.transactionOutcome.value } returns 25.0
        launchFragmentInHiltContainer<MainScreen> {
            coEvery { mainMenuViewModel.transactionIncome.value } returns 222.0
        }
//        coEvery { mainMenuViewModel.transactionIncome.value } returns 222.0
        every { mainMenuViewModel.userID.value } returns 4

        onView(ViewMatchers.withId(R.id.income_amount)).check(
            ViewAssertions.matches(
                ViewMatchers.withText("12.0")
            )
        )

    }
}