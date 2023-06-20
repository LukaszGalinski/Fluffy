package com.lukasz.galinski.fluffy

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.lukasz.galinski.fluffy.framework.database.transaction.TransactionUseCases
import com.lukasz.galinski.fluffy.framework.database.user.UserUseCases
import com.lukasz.galinski.fluffy.framework.preferences.PreferencesData
import com.lukasz.galinski.fluffy.presentation.main.MainScreen
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class MainMenuScreenUITest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockedUserUseCases = mockk<UserUseCases>()
    private val mockedTransactionUseCases = mockk<TransactionUseCases>()
    private val userPreferences = mockk<PreferencesData>(relaxed = true)

    @BindValue
    val mainMenuViewModel: MainMenuViewModel = spyk(
        MainMenuViewModel(
            mockedUserUseCases,
            mockedTransactionUseCases,
            userPreferences,
            testDispatcher
        )
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun prepareView() {
        hiltRule.inject()
    }

    @Test
    fun checkIncomeValueDisplayedOnHomeScreen() {
        val mockedAmount = 882.2
        every { mainMenuViewModel.transactionIncome } returns MutableStateFlow(mockedAmount)

        launchFragmentInHiltContainer<MainScreen>()

        onView(withId(R.id.income_amount)).check(matches(withText("$$mockedAmount")))
    }

    @Test
    fun checkOutcomeValueDisplayedOnHomeScreen() {
        val mockedAmount = 999.55
        every { mainMenuViewModel.transactionOutcome } returns MutableStateFlow(mockedAmount)

        launchFragmentInHiltContainer<MainScreen>()

        onView(withId(R.id.outcome_amount)).check(matches(withText("$$mockedAmount")))
    }

    @Test
    fun checkBalanceValueDisplayedOnHomeScreen() {
        val mockedAmount = 7878.73
        every { mainMenuViewModel.accountBalance } returns MutableStateFlow(mockedAmount)

        launchFragmentInHiltContainer<MainScreen>()

        onView(withId(R.id.account_balance)).check(matches(withText("$$mockedAmount")))
    }
}
