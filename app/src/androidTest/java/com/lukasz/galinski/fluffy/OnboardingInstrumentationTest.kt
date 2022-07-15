package com.lukasz.galinski.fluffy

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lukasz.galinski.fluffy.view.onboarding.OnBoardingScreenActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

private const val SWIPE_DURATION = 1000L

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class OnboardingInstrumentationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule: ActivityScenarioRule<OnBoardingScreenActivity> =
        ActivityScenarioRule(OnBoardingScreenActivity::class.java)

    @Before
    fun prepareView() {
        hiltRule.inject()
    }

    @Test
    fun checkLoginButtonDisplayed() {
        onView(withId(R.id.onboarding_login_button)).check(
            matches(
                allOf(
                    isDisplayed(),
                    withText(R.string.login)
                )
            )
        )
    }

    @Test
    fun checkRegisterButtonDisplayed() {
        onView(withId(R.id.onboarding_register_button)).check(matches(withText(R.string.register)))
    }

    @Test
    fun checkTitleTextOnSeparateScreens() {
        onView(withId(R.id.onboarding_title)).check(matches(withText(R.string.onBoarding_one_title)))
        onView(withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        sleep(SWIPE_DURATION)
        onView(
            allOf(
                withId(R.id.onboarding_title),
                isDisplayed()
            )
        ).check(matches(isCompletelyDisplayed()))
            .check(matches(withText(R.string.onBoarding_two_title)))
        onView(withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        sleep(SWIPE_DURATION)
        onView(
            allOf(
                withId(R.id.onboarding_title),
                isDisplayed()
            )
        ).check(matches(withText(R.string.onBoarding_three_title)))
    }

    @Test
    fun checkSubTitleTextOnSeparateScreens() {
        onView(withId(R.id.onboarding_subTitle)).check(matches(withText(R.string.onBoarding_one_subtitle)))
        onView(withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        sleep(SWIPE_DURATION)
        onView(
            allOf(
                withId(R.id.onboarding_subTitle),
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        )
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText(R.string.onBoarding_two_subtitle)))
        onView(withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        sleep(SWIPE_DURATION)
        onView(
            allOf(
                withId(R.id.onboarding_subTitle),
                withEffectiveVisibility(Visibility.VISIBLE)
            )
        )
            .check(matches(isCompletelyDisplayed()))
            .check(matches(withText(R.string.onBoarding_three_subtitle)))
    }

    @Test
    fun checkImageTagOnSeparateScreens() {
        onView(withId(R.id.onboarding_logo)).check(matches(withTagValue(equalTo(R.drawable.onboarding_one_logo))))
        onView(withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        sleep(SWIPE_DURATION)
        onView(withId(R.id.onboarding_logo)).check(matches(withTagValue(equalTo(R.drawable.onboarding_two_logo))))
        onView(withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        sleep(SWIPE_DURATION)
        onView(withId(R.id.onboarding_logo)).check(matches(withTagValue(equalTo(R.drawable.onboarding_three_logo))))
    }
}