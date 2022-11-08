package com.lukasz.galinski.fluffy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lukasz.galinski.fluffy.model.TransactionModel
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.repository.database.transaction.TransactionsRepositoryImpl
import com.lukasz.galinski.fluffy.repository.database.user.UsersRepositoryImpl
import com.lukasz.galinski.fluffy.repository.preferences.PreferencesData
import com.lukasz.galinski.fluffy.view.main.MainScreen
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MainMenuScreenUITest {

    private val testDispatcher = StandardTestDispatcher()
    private val transactionRepository = mockk<TransactionsRepositoryImpl>()
    private val userRepository = mockk<UsersRepositoryImpl>()
    private val mockedUser = mockk<UserModel>()
    private val userPreferences = mockk<PreferencesData>(relaxed = true)
    private lateinit var mainMenuViewModel: MainMenuViewModel
    private val mockedTransaction = mockk<TransactionModel>(relaxed = true)

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
    fun checkIncomeAndOutcomePriceDisplayed() {
        launchFragmentInHiltContainer<MainScreen> {
            //coEvery { mainMenuViewModel.transactionIncome.value.toString() } returns "2521"
            //coEvery { mainMenuViewModel.transactionIncome.value } returns 2222
        }
        //coEvery { mainMenuViewModel.transactionIncome.value } returns 222
        every { mainMenuViewModel.userID.value } returns 4
        

        mainMenuViewModel.transactionIncome.value = 12

        Thread.sleep(2000)

        onView(ViewMatchers.withId(R.id.income_amount)).check(
            ViewAssertions.matches(
                ViewMatchers.withText("222")
            )
        )

    }
}