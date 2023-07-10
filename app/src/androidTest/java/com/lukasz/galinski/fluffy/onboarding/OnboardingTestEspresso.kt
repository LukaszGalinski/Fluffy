package com.lukasz.galinski.fluffy.onboarding

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.presentation.account.AccountHostActivity
import com.lukasz.galinski.fluffy.presentation.common.LoginEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class OnboardingTestEspresso {

    private val swipeDuration = 1000L
    private val activity = AccountHostActivity.createIntent(
        ApplicationProvider.getApplicationContext(), LoginEntryPoint.ONBOARDING
    )

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule: ActivityScenarioRule<AccountHostActivity> = ActivityScenarioRule(activity)

    @Before
    fun prepareView() {
        hiltRule.inject()
    }

    @Test
    fun checkLoginButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_login_button)).check(
            ViewAssertions.matches(
                Matchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.withText(R.string.login)
                )
            )
        )
    }

    @Test
    fun checkRegisterButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_register_button))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.register)))
    }

    @Test
    fun checkTitleTextOnSeparateScreens() {
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.onBoarding_one_title)))
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        Thread.sleep(swipeDuration)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.onboarding_title),
                ViewMatchers.isDisplayed()
            )
        ).check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.onBoarding_two_title)))
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        Thread.sleep(swipeDuration)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.onboarding_title),
                ViewMatchers.isDisplayed()
            )
        ).check(ViewAssertions.matches(ViewMatchers.withText(R.string.onBoarding_three_title)))
    }

    @Test
    fun checkSubTitleTextOnSeparateScreens() {
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_subTitle))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.onBoarding_one_subtitle)))
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        Thread.sleep(swipeDuration)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.onboarding_subTitle),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.onBoarding_two_subtitle)))
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        Thread.sleep(swipeDuration)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.onboarding_subTitle),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
            .check(ViewAssertions.matches(ViewMatchers.withText(R.string.onBoarding_three_subtitle)))
    }

    @Test
    fun checkImageTagOnSeparateScreens() {
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_logo))
            .check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo(R.drawable.onboarding_one_logo))))
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        Thread.sleep(swipeDuration)
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_logo))
            .check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo(R.drawable.onboarding_two_logo))))
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_fragment_host)).perform(ViewActions.swipeLeft())
        Thread.sleep(swipeDuration)
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_logo))
            .check(ViewAssertions.matches(ViewMatchers.withTagValue(Matchers.equalTo(R.drawable.onboarding_three_logo))))
    }
}