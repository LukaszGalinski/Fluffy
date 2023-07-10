package com.lukasz.galinski.fluffy.onboarding

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.launchFragmentInHiltContainer
import com.lukasz.galinski.fluffy.presentation.onboarding.OnboardingParentFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class OnboardingParentFragmentTest {

    private val navController = mockk<NavController>(relaxed = true)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun prepareView() {
        hiltRule.inject()

        launchFragmentInHiltContainer<OnboardingParentFragment> {
            Navigation.setViewNavController(this.requireView(), navController)
        }
    }

    @Test
    fun checkRegisterButtonText() {
        onView(withId(R.id.onboarding_register_button)).check(matches(withText("Sign Up")))
    }

    @Test
    fun checkRegisterButtonNavigatesToRegisterScreen() {
        onView(withText(R.string.register)).perform(click())

        verify(exactly = 1) { navController.navigate(R.id.action_onboardingParentFragment_to_registerScreen) }
    }

    @Test
    fun checkLoginButtonDisplayed() {
        onView(withId(R.id.onboarding_login_button)).check(matches(withText("Login")))
    }

    @Test
    fun checkLoginButtonNavigatesToLoginScreen() {
        onView(withText(R.string.login)).perform(click())

        verify(exactly = 1) { navController.navigate(R.id.action_onboardingParentFragment_to_loginScreen) }
    }

    @Test
    fun checkTitleTextOnInitialScreen() {
        onView(withId(R.id.onboarding_title)).check(matches(withText("Gain total control of your money")))
    }

    @Test
    fun checkSubTitleTextOnInitialScreen() {
        onView(withId(R.id.onboarding_subTitle)).check(matches(withText("Become your own money manager and make every cent count")))
    }

    @Test
    fun checkImageTagOnSeparateScreens() {
        onView(withId(R.id.onboarding_logo)).check(matches(withTagValue(equalTo(R.drawable.onboarding_one_logo))))
    }
}
