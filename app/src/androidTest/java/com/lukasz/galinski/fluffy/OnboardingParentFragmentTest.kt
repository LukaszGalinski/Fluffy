package com.lukasz.galinski.fluffy

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.lukasz.galinski.fluffy.presentation.onboarding.OnboardingParentFragment
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class OnboardingParentFragmentTest {

    @BindValue
    val onboardingParentFragment: OnboardingParentFragment = OnboardingParentFragment()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun prepareView() {
        hiltRule.inject()

        launchFragmentInHiltContainer<OnboardingParentFragment> { onboardingParentFragment }
    }

    @Test
    fun checkRegisterButtonDisplayed() {
        onView(withId(R.id.onboarding_register_button))
            .check(matches(withText(R.string.register)))
    }

    @Test
    fun checkLoginButtonDisplayed() {
        onView(withId(R.id.onboarding_login_button))
            .check(matches(withText(R.string.login)))
    }

    @Test
    fun checkTitleTextOnInitialScreen() {
        onView(withId(R.id.onboarding_title))
            .check(matches(withText(R.string.onBoarding_one_title)))
    }

    @Test
    fun checkSubTitleTextOnInitialScreen() {
        onView(withId(R.id.onboarding_subTitle))
            .check(matches(withText(R.string.onBoarding_one_subtitle)))
    }

    @Test
    fun checkImageTagOnSeparateScreens() {
        onView(withId(R.id.onboarding_logo))
            .check(matches(withTagValue(equalTo(R.drawable.onboarding_one_logo))))
    }
}
